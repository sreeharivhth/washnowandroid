package wash.midest.com.mrwashapp.mrwashapp;

import android.app.Application;

import wash.midest.com.mrwashapp.appservices.RxEventBus;

/**
 * Created by Sreehari.KV on 3/27/2018.
 */

public class MrWashApp extends Application {

    private RxEventBus rxEventBus;
    @Override
    public void onCreate() {
        super.onCreate();
        rxEventBus=new RxEventBus();
    }

    public RxEventBus getRxEventBus(){
        return rxEventBus;
    }
}
