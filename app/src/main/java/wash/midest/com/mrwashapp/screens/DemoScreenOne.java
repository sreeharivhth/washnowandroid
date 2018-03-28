package wash.midest.com.mrwashapp.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.functions.Consumer;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;

public class DemoScreenOne extends AppCompatActivity {

    boolean didReceivedObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_screen_one);

        ((MrWashApp) getApplication())
        .getRxEventBus()
        .toObservable()
        .subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {

                if(!didReceivedObject) {
                    Log.d("DemoScreenOne ====", "DemoScreenOne accept method called ");
                    if (o instanceof GeneralListDataPojo) {
                        GeneralListDataPojo generalListDataPojo = (GeneralListDataPojo) o;
                        Log.d("DemoScreenOne ====", "DemoScreenOne ==== " + generalListDataPojo.getStatus());
                    } else {
                        Log.d("DemoScreenOne ====", "Not instance of DemoScreenOne ==== ");
                    }
                    didReceivedObject=true;
                }else{
                    Log.d("DemoScreenOne ====","DemoScreenOne didReceivedObject=true ");
                }
            }
        })
        ;

    }
}
