package com.example.wael.test2;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    Context providesContext() {
        return mActivity;
    }

    @Provides
    Activity providesActivity() {
        return mActivity;
    }
}
