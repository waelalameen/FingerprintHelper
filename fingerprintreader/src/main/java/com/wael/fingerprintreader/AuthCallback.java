package com.wael.fingerprintreader;

import android.hardware.fingerprint.FingerprintManager;

public interface AuthCallback {
    void onAuthSucceeded(FingerprintManager.AuthenticationResult result);
    void onAuthError(CharSequence errString);
    void onAuthFailed();
    void onAuthHelp(CharSequence helpString);
    void isHardwareDetected(boolean isDetected);
    void hasEnrolledFingerprints(boolean hasEnrolled);
    void isKeyguardSecure(boolean isKeySecure);
}
