package wash.midest.com.mrwashapp.mrwashapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class AppEventListener implements LifecycleObserver {

    final String TAG = AppEventListener.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void gotCreated(){
        Log.d(TAG,"AppEventListener Created");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void gotStarted(){
        Log.d(TAG,"AppEventListener Started");
        MrWashApp.getMrWashApp().setAppActive(true);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void gotStopped(){
        Log.d(TAG,"AppEventListener Stopped");
        MrWashApp.getMrWashApp().setAppActive(false);
    }
}
