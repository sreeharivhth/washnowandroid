package wash.midest.com.mrwashapp.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;
import wash.midest.com.mrwashapp.screens.fragmentviews.ContactFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.FAQFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.LandingFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.MyOrderFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.MyProfileFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.PriceListFrag;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

public class LandingActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleText;
    private LinearLayout mScrollLinearView;
    private boolean mDidReceivedObject=false;
    private GeneralListDataPojo mServicesData;
    private APIConstants mApiConstants;
    private String TAG=LandingActivity.class.getName();
    private LinearLayout mLandingReplaceableContent;
    private LandingFrag mDirectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Disable default title which will be near to navigation drawer icon
        setTitle("");
        //Set custom title in center of screen
        mTitleText=findViewById(R.id.toolbar_title);
        mTitleText.setText(getString(R.string.app_title));

        mLandingReplaceableContent = findViewById(R.id.landing_view);
        mScrollLinearView=(LinearLayout) findViewById(R.id.landingScrollLinearView);
        mApiConstants=new APIConstants();
        Log.d(TAG,TAG+" LandingActivity launched");
        mServicesData = getIntent().getExtras().getParcelable("LandingData");
        replaceLandingContent(mServicesData);
    }

    private void replaceLandingContent(GeneralListDataPojo mServicesData){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        mDirectFragment = LandingFrag.newInstance(mServicesData);
        fragTrans.replace(R.id.landing_view, mDirectFragment);
        fragTrans.addToBackStack("LandingFrag");
        fragTrans.commit();
        mDirectFragment.setUserVisibleHint(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            FragmentManager fm = getSupportFragmentManager();
            int currentCount = fm.getBackStackEntryCount();
            //If count is 1 , its landing fragment
            if(currentCount==1){
                onExit();
                return;
            }
            for (Fragment frag : fm.getFragments()) {

                if (frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        for (Fragment childfragnested: childFm.getFragments()) {
                            FragmentManager childFmNestManager = childfragnested.getFragmentManager();
                            if(childfragnested.isVisible()) {
                                childFmNestManager.popBackStack();
                                return;
                            }
                        }
                    }
                }
            }
            super.onBackPressed();
        }
    }

    private void onExit(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        alertDialog2.setTitle("");
        alertDialog2.setMessage(R.string.exit_confirm);
        alertDialog2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        alertDialog2.show();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            //Maintain count as 1 while changing the menu options
            popTillBackStack(1);
            replaceLandingContent(null, MyProfileFrag.newInstance(),"MyProfile");

        } else if (id == R.id.nav_orders) {

            popTillBackStack(1);
            replaceLandingContent(null, MyOrderFrag.newInstance(),"MyOrderFrag");

        } else if (id == R.id.nav_price) {
            if(isConnectedToNet()) {
                popTillBackStack(1);
                int defaultSelection = -1;
                replaceLandingContent(null, PriceListFrag.newInstance(getServiceTypes(),defaultSelection), "PriceListFrag");
            }
        } else if (id == R.id.nav_offers) {
            if(isConnectedToNet()){

            }
        } else if (id == R.id.nav_faq) {
            if(isConnectedToNet()){
                popTillBackStack(1);
                replaceLandingContent(null, FAQFrag.newInstance(),"FAQFrag");
            }
        } else if (id == R.id.nav_contact) {
            if(isConnectedToNet()){
                popTillBackStack(1);
                replaceLandingContent(null, ContactFrag.newInstance(),"ContactFrag");
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<WashTypes> getServiceTypes(){
        ArrayList<WashTypes> types = new ArrayList<>();
        for(int count=0;count<mServicesData.getData().size();count++){
            Data serviceData = mServicesData.getData().get(count);
            if(serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)){
                WashTypes washType=new WashTypes();
                washType.setId(Integer.parseInt(serviceData.getId()));
                washType.setTime("@ "+serviceData.getDeliveryTime()+" hrs");
                washType.setWashType(serviceData.getName());
                types.add(washType);
            }
        }
        return types;
    }
    /*private void popAllFragBackStack(String name){
        FragmentManager fragMan = getSupportFragmentManager();
        if(TextUtils.isEmpty(name))
            fragMan.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else
            fragMan.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }*/

    public void pushPriceListWithIndex(int index){
        if(isConnectedToNet()) {
            popTillBackStack(1);
            replaceLandingContent(null, PriceListFrag.newInstance(getServiceTypes(),index), "PriceListFrag");
        }
    }
    public void setFragmentTitle(String title){
        if(!TextUtils.isEmpty(title))
        mTitleText.setText(title);
    }
    private void popTillBackStack(int count){
        FragmentManager fm = getSupportFragmentManager();
        int totalBackStack = fm.getBackStackEntryCount();
        for(int i = totalBackStack; i > count ; --i) {
            fm.popBackStack();
        }
    }
    private void replaceLandingContent(GeneralListDataPojo mServicesData,Fragment fragment,String tag){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.replace(R.id.landing_view, fragment);
        fragTrans.addToBackStack(tag);
        fragTrans.commit();
    }
    private boolean isConnectedToNet(){
        boolean status = mAppUtils.isNetworkConnected(this);
        if(!status){
            showErrorAlert(getString(R.string.network_error));
        }
        return status;
    }

    /*private class ViewPagerAdapter extends FragmentPagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ViewPagerFrag.newInstance("20","ON FIRST ORDER");
                case 1:
                    return ViewPagerFrag.newInstance("30","ON SECOND ORDER");
                case 2:
                    return ViewPagerFrag.newInstance("40","ON THIRD ORDER");
                case 3:
                    return ViewPagerFrag.newInstance("50","ON FOURTH ORDER");
                default:
                    return ViewPagerFrag.newInstance("def","def");
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    }*/
}
