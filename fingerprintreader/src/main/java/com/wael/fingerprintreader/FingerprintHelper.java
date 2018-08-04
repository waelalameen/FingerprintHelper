package com.wael.fingerprintreader;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * FingerprintHelper class to help detecting and reading fingerprints
 * By Wael Alameen<wael.almeen@gmail.com>
 */
public class FingerprintHelper {

    private static final String LOG = FingerprintHelper.class.getSimpleName();
    private Cipher cipher;
    private KeyStore keyStore;
    private static final String KEY_NAME = "yourKey";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    @RequiresApi(api = Build.VERSION_CODES.M)
    private FingerprintHelper(Context context, AuthCallback authCallback) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

        if (fingerprintManager != null && keyguardManager != null) {
            if (!fingerprintManager.isHardwareDetected()) {
                authCallback.isHardwareDetected(false);
            } else {
                authCallback.isHardwareDetected(true);
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                authCallback.hasEnrolledFingerprints(false);
            } else {
                authCallback.hasEnrolledFingerprints(true);
            }

            if (!keyguardManager.isKeyguardSecure()) {
                authCallback.isKeyguardSecure(false);
            } else {
                try {
                    generateKey();
                } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | NoSuchProviderException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler handler = new FingerprintHandler(context, authCallback);
                    handler.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getFingerprintReader(Context context, AuthCallback authCallback) {
        new FingerprintHelper(context, authCallback);
    }

    private void generateKey() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, IOException, CertificateException {
        keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
            keyStore.load(null);

            try {
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());

                keyGenerator.generateKey();

            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyStoreException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to init cipher", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private CancellationSignal cancellationSignal;
        private Context context;
        private AuthCallback authCallback;

        FingerprintHandler(Context context, AuthCallback authCallback) {
            this.context = context;
            this.authCallback = authCallback;
        }

        void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            cancellationSignal = new CancellationSignal();

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Log.d(LOG, "Authentication Succeeded");
            authCallback.onAuthSucceeded(result);
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.e(LOG, "Authentication Error");
            // cancellationSignal.cancel();
            authCallback.onAuthError(errString);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Log.e(LOG, "Authentication Failed");
            // cancellationSignal.cancel();
            authCallback.onAuthFailed();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Log.d(LOG, "Authentication Help");
            authCallback.onAuthHelp(helpString);
        }
    }
}
