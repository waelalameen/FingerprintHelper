package com.example.wael.test2;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;
    @Inject
    DataManager dataManager;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
