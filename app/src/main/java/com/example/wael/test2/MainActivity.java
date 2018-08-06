package com.example.wael.test2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.wael.test2.databinding.ActivityMainBinding;
import com.example.wael.test2.services.FingerprintListener;
import com.wael.fingerprintreader.AuthCallback;
import com.wael.fingerprintreader.FingerprintHelper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private boolean isOpen = false;
    @Inject
    DataManager dataManager;

    private ActivityComponent activityComponent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // startService(new Intent(this, FingerprintListener.class));

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        mBinding.fab.setOnClickListener(view -> {
//            if (isOpen) {
//                ((FloatingActionButton) view).setImageResource(R.drawable.ic_add_black_24dp);
//                AnimationHelper.endRevealAnimation(mBinding.contentLayout, mBinding.buttonLayout);
//                isOpen = false;
//            } else {
//                ((FloatingActionButton) view).setImageResource(R.drawable.ic_close_black_24dp);
//                AnimationHelper.startRevealAnimation(mBinding.contentLayout, mBinding.buttonLayout);
//                isOpen = true;
//            }
//        });

        // getActivityComponent().inject(this);

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
                    Log.d(LOG, "Keyguard Secure");
                }
            }
        });
    }

    public ActivityComponent getActivityComponent() {
//        if (activityComponent == null) {
//            activityComponent = DaggerActivityComponent
//                    .builder()
//                    .activityModule(new ActivityModule(this))
//                    .applicationComponent(MyApplication.get(this).getApplicationComponent())
//                    .build();
//        }
        return activityComponent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stopService(new Intent(this, FingerprintListener.class));
    }
}
