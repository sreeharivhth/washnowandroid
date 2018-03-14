package wash.midest.com.mrwashapp.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import wash.midest.com.mrwashapp.R;

public class PlaceOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.place_order_title),true);
    }
}
