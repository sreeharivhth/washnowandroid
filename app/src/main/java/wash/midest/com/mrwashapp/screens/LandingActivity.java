package wash.midest.com.mrwashapp.screens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleText;
    private LinearLayout mScrollLinearView;
    //LandingHorizontalView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        addHorizontalViews(50);
    }

    private void addHorizontalViews(int viewCount){

        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(this);
            if(count%2==0){
                horizontalView.setBackgroundColor(getResources().getColor(R.color.landing_hori_back_two));
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
            super.onBackPressed();
        }
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
