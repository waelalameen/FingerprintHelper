package com.example.wael.test2;

import android.app.Application;
import android.content.Context;

import com.example.wael.test2.helpers.SharedPrefHelper;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefHelper getPreferneceHelper();
}
