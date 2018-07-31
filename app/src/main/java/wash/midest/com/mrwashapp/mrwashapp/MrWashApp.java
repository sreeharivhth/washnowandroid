package wash.midest.com.mrwashapp.mrwashapp;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import wash.midest.com.mrwashapp.appservices.RxEventBus;

/**
 * Created by Sreehari.KV on 3/27/2018.
 */

public class MrWashApp extends MultiDexApplication {

    public static MrWashApp mrWashApp;
    private  boolean isAppActive;

    @Override
    public void onCreate() {
        super.onCreate();
        mrWashApp=this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppEventListener());
    }

    public boolean isAppActive() {
        return isAppActive;
    }

    public void setAppActive(boolean appActive) {
        isAppActive = appActive;
    }

    public static MrWashApp getMrWashApp() {
        return mrWashApp;
    }

    @Override
    public void onTerminate (){
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }



}
