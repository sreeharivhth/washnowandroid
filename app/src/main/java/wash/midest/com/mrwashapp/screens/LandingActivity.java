package wash.midest.com.mrwashapp.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import wash.midest.com.mrwashapp.screens.fragmentviews.LandingFrag;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleText;
    private LinearLayout mScrollLinearView;
    private boolean mDidReceivedObject=false;
    private GeneralListDataPojo mServicesData;
    private APIConstants mApiConstants;
    private String TAG=LandingActivity.class.getName();
    private LinearLayout mLandingReplaceableContent;
    private Fragment mDirectFragment;

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

        /*ViewPager pager = (ViewPager) findViewById(R.id.viewpager_home);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));*/
        mScrollLinearView=(LinearLayout) findViewById(R.id.landingScrollLinearView);
        mApiConstants=new APIConstants();
        Log.d(TAG,TAG+" LandingActivity launched");
        mServicesData = getIntent().getExtras().getParcelable("LandingData");
        /*populateWashTypes();*/
        replaceLandingContent(mServicesData);
    }

    private void replaceLandingContent(GeneralListDataPojo mServicesData){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        LandingFrag fragA = LandingFrag.newInstance(mServicesData);
        mDirectFragment = fragA;
        fragTrans.add(R.id.landing_view, fragA);
        fragTrans.addToBackStack("LandingFrag");
        fragTrans.commit();
    }

    /*private void populateWashTypes(){
        Log.d(TAG,TAG+" populateWashTypes");

        ArrayList<WashTypes> types = new ArrayList<>();
        if(null!=mServicesData){
            Log.d(TAG,TAG+" populateWashTypes null!=mServicesData");
            Log.d(TAG,TAG+" populateWashTypes mServicesData.getData().size() ="+mServicesData.getData().size());
            for(int count=0;count<mServicesData.getData().size();count++){
                Data serviceData = mServicesData.getData().get(count);
                if(serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)){
                    WashTypes washType=new WashTypes();
                    washType.setTime("@ "+serviceData.getDeliveryTime()+" hrs");
                    washType.setWashType(serviceData.getName());
                    types.add(washType);
                }
            }
            Log.d(TAG,TAG+" populateWashTypes Adding horizontal news");
            addHorizontalViews(types.size(),types);
        }else{
            Log.d(TAG,TAG+" mServicesData is null");
        }
        *//*WashTypes washType1=new WashTypes();
        washType1.setTime("@24hrs");
        washType1.setWashType("QUICK WASH");

        WashTypes washType2=new WashTypes();
        washType2.setTime("@48hrs");
        washType2.setWashType("NORMAL WASH");

        WashTypes washType3=new WashTypes();
        washType3.setTime("@24hrs");
        washType3.setWashType("QUICK IRON");

        WashTypes washType4=new WashTypes();
        washType4.setTime("@48hrs");
        washType4.setWashType("NORMAL IRON");

        types.add(washType1);
        types.add(washType2);
        types.add(washType3);
        types.add(washType4);*//*
    }*/

    /*private void addHorizontalViews(int viewCount,ArrayList<WashTypes> types){

        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(this,count,types.get(count));
            if(count%2!=0){
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        horizontalView.setBackground(getDrawable(R.drawable.list_background_one));
                    }else{
                        horizontalView.setBackgroundResource(R.drawable.list_background_one);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        horizontalView.setBackground(getDrawable(R.drawable.list_background_two));
                    }else{
                        horizontalView.setBackgroundResource(R.drawable.list_background_two);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mScrollLinearView.addView(horizontalView);
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            /*if(mDirectFragment!=null && mDirectFragment.getChildFragmentManager().getBackStackEntryCount()>0){
                mDirectFragment.getChildFragmentManager().popBackStack();
            }else if(mDirectFragment instanceof LandingFrag){
                onExit();
            }else{
                super.onBackPressed();
            }*/

            FragmentManager fm = getSupportFragmentManager();
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
                    else if(frag instanceof LandingFrag && frag.getUserVisibleHint() && frag.isResumed()){
                        Toast.makeText(this,"LandingFrag detected",Toast.LENGTH_SHORT).show();
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
            // Handle the camera action
        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_price) {

        } else if (id == R.id.nav_offers) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_contact) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
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
    }
}
