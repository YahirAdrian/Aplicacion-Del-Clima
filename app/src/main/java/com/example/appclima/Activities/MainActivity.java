package com.example.appclima.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.appclima.Adapters.ViewPagerAdapter;
import com.example.appclima.Fragments.DataFragment;
import com.example.appclima.Fragments.MapFragment;
import com.example.appclima.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
//6:27
public class MainActivity extends AppCompatActivity implements MapFragment.DataListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        configurateTabs();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setViewPager(viewPager);
    }

    private void setToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void configurateTabs()
    {
        tabLayout = (TabLayout) findViewById(R.id.tabLyout);
        tabLayout.addTab(tabLayout.newTab().setText("Clima"));
        tabLayout.addTab(tabLayout.newTab().setText("Ubicacion"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    }

    private void setViewPager(final ViewPager viewPager)
    {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public void sendData(LatLng coordinates){
        DataFragment fragmentData = (DataFragment) getSupportFragmentManager().getFragments().get(0);
        fragmentData.reciveCoordinates(coordinates);
        viewPager.setCurrentItem(0);

    }
}
