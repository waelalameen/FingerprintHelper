package com.example.wael.test2.services;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.wael.fingerprintreader.AuthCallback;
import com.wael.fingerprintreader.FingerprintHelper;

public class FingerprintListener extends IntentService {

    private static final String LOG = FingerprintListener.class.getSimpleName();

    public FingerprintListener() {
        super(FingerprintListener.class.getSimpleName());
        Log.d(LOG, "Service Started");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG, "Intent Started");

        FingerprintHelper.getFingerprintReader(this, new AuthCallback() {
            @Override
            public void onAuthSucceeded(FingerprintManager.AuthenticationResult result) {
                Log.d(LOG + " Success", result.getCryptoObject().getCipher().getAlgorithm());
            }

            @Override
            public void onAuthError(CharSequence errString) {
                Log.d(LOG, errString.toString());
            }

            @Override
            public void onAuthFailed() {
                Log.d(LOG, "Auth Failed");
            }

            @Override
            public void onAuthHelp(CharSequence helpString) {
                Log.d(LOG, helpString.toString());

            }

            @Override
            public void isHardwareDetected(boolean isDetected) {
                if (isDetected) {
                    Log.d(LOG, "Hardware Detected");
                } else {
                    Log.d(LOG, "Hardware Not Detected");
                }
            }

            @Override
            public void hasEnrolledFingerprints(boolean hasEnrolled) {
                if (hasEnrolled) {
                    Log.d(LOG, "Has Enrolled Fingerprints");
                } else {
                    Log.d(LOG, "Has Not Enrolled Fingerprints");
                }
                // startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
            }

            @Override
            public void isKeyguardSecure(boolean isKeySecure) {
                if (isKeySecure) {
                    Log.d(LOG, "is Keyguard Secure");
                }
            }
        });
    }
}
