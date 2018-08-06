package com.example.wael.test2;

import android.content.Context;

import com.example.wael.test2.helpers.SharedPrefHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private Context context;
    private SharedPrefHelper msharedPrefHelper;

    @Inject
    DataManager(@ApplicationContext Context context, SharedPrefHelper msharedPrefHelper) {
        this.context = context;
        this.msharedPrefHelper = msharedPrefHelper;
    }

    public void saveAccessToken(String accessToken) {
        msharedPrefHelper.put(SharedPrefHelper.PREF_TOKEN_KEY, accessToken);
    }

    public String getAccessToken() {
        return msharedPrefHelper.get(SharedPrefHelper.PREF_TOKEN_KEY, null);
    }
}
