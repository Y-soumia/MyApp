package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youbi.myapp.adapters.PagerAdapter;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;
import com.facebook.drawee.backends.pipeline.Fresco;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private GetDataService service;
    private DrawerLayout mDrawerLayout;
    private PagerSlidingTabStrip tabStrip;
    private SharedPreferences sp;
    private Menu menu;

    private static final String TAG = "MainActivity : ";

    private FragmentPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        context = getBaseContext();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.viewPager);
        tabStrip = findViewById(R.id.tabs);
        sp = context.getSharedPreferences("login", MODE_PRIVATE);

        if (!isConnectedToInternet()) {
            Intent intent = new Intent(context, NoInternetActivity.class);
            startActivity(intent);
        } else {

            Fresco.initialize(this);

            setSupportActionBar(toolbar);
            actionbar = this.getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

            setNavigationViewListner();

            viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOffscreenPageLimit(4);

            tabStrip.setShouldExpand(true);
            tabStrip.setViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Toast.makeText(MainActivity.this,
                            "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    // Opening the drawer when clicking the menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // clicking the drawer items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_my_acc: {
                Intent intent;
                if (!sp.getString("token", "").equals("")) {
                    intent = new Intent(context, MyAccountActivity.class);
                } else {
                    intent = new Intent(context, LoginActivity.class);
                }
                startActivity(intent);
                break;
            }
            case R.id.nav_ad: {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_contact: {
                Intent intent = new Intent(context, MyAccountActivity.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
