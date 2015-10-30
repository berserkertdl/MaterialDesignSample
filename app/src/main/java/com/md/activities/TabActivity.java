package com.md.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.md.R;
import com.md.adapters.FragmentsAdapter;
import com.md.fragments.Tab1Fragment;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends BaseActionBarActivity {


    private TabLayout tabLayout;

    private ViewPager viewPager;

    private FragmentsAdapter fragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // 切换卡片
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        /*tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 5"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 6"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 7"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 8"));*/

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<String> fragmentTitles = new ArrayList<String>();
        fragmentTitles.add("tab1");
        fragmentTitles.add("tab2");
        fragmentTitles.add("tab3");
        fragmentTitles.add("tab4");
        fragmentTitles.add("tab5");
        fragmentTitles.add("tab6");
        fragmentTitles.add("tab7");
        fragmentTitles.add("tab8");

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(Tab1Fragment.newInstance("tab1", ""));
        fragments.add(Tab1Fragment.newInstance("tab2", ""));
        fragments.add(Tab1Fragment.newInstance("tab3", ""));
        fragments.add(Tab1Fragment.newInstance("tab4", ""));
        fragments.add(Tab1Fragment.newInstance("tab5", ""));
        fragments.add(Tab1Fragment.newInstance("tab6", ""));
        fragments.add(Tab1Fragment.newInstance("tab7", ""));
        fragments.add(Tab1Fragment.newInstance("tab8", ""));
        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(),fragments,fragmentTitles);
        viewPager.setAdapter(fragmentsAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
