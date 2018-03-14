package wash.midest.com.mrwashapp.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleText;
    private LinearLayout mScrollLinearView;

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

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager_home);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mScrollLinearView=(LinearLayout) findViewById(R.id.landingScrollLinearView);

        ArrayList<WashTypes> washTypes=getWashTypes();

        addHorizontalViews(washTypes.size(),washTypes);
    }

    private ArrayList<WashTypes> getWashTypes(){

        ArrayList<WashTypes> types = new ArrayList<>();
        WashTypes washType1=new WashTypes();
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
        types.add(washType4);

        return types;
    }

    private void addHorizontalViews(int viewCount,ArrayList<WashTypes> types){

        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(this,count,types.get(count));
            if(count%2!=0){
                horizontalView.setBackground(getDrawable(R.drawable.list_background_one));
            }else{
                horizontalView.setBackground(getDrawable(R.drawable.list_background_two));
            }
            mScrollLinearView.addView(horizontalView);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            onExit();
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
