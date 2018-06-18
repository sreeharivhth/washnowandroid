package wash.midest.com.mrwashapp.mrwashapp;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import wash.midest.com.mrwashapp.appservices.RxEventBus;

/**
 * Created by Sreehari.KV on 3/27/2018.
 */

public class MrWashApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //registerActivityLifecycleCallbacks(new WashActivityLifecycleCallBack());
    }

    @Override
    public void onTerminate (){
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }


    private static final class WashActivityLifecycleCallBack implements ActivityLifecycleCallbacks{

        private static final String TAG="MrWashApp ";
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG,"onActivityCreated for "+activity.getLocalClassName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG,"onActivityStarted for "+activity.getLocalClassName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG,"onActivityResumed for "+activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG,"onActivityPaused for "+activity.getLocalClassName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG,"onActivityStopped for "+activity.getLocalClassName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG,"onActivitySaveInstanceState for "+activity.getLocalClassName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG,"onActivityDestroyed for "+activity.getLocalClassName());
        }
    }
}
