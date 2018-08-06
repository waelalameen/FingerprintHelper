package com.example.wael.test2;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/*
@PerActivity is a scope and is used to tell the Dagger that the Context and Activity
provided by the ActivityModule will be instantiated each time an Activity is created.
So, these objects persist till that activity lives and each activity has its own set of these.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    @ApplicationContext
    Context getContext();

    Application getApplication();
}
