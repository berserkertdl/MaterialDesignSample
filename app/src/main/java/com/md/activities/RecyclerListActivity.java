package com.md.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.md.R;
import com.md.adapters.ApplicationAdapter;
import com.md.entity.AppInfo;
import com.md.itemanimator.CustomItemAnimator;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerListActivity extends BaseActionBarActivity {


    private RecyclerView mRecyclerView;
    private ApplicationAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;

    private Toolbar mToolbar;

    private List<AppInfo> applicationList = new ArrayList<AppInfo>();

    private static final int DRAWER_ITEM_SWITCH = 1;
    private static final int DRAWER_ITEM_OPEN_SOURCE = 10;

    private Drawer drawer;  //侧拉框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);

    }

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        super.initView();

        final SharedPreferences pref = getSharedPreferences("com.mikepenz.applicationreader", 0);

        drawer = new DrawerBuilder(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new SwitchDrawerItem().withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton compoundButton, boolean b) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("autouploadenabled", b);
                                editor.apply();
                            }
                        }).withName(R.string.drawer_switch).withChecked(pref.getBoolean("autouploadenabled", false))
                ).addStickyDrawerItems(
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_opensource)
                                .withIdentifier(DRAWER_ITEM_OPEN_SOURCE)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == DRAWER_ITEM_OPEN_SOURCE) {
                            new LibsBuilder()
                                    .withFields(R.string.class.getFields())
                                    .withVersionShown(true)
                                    .withLicenseShown(true)
                                    .withActivityTitle(getString(R.string.drawer_opensource))
                                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                    .start(RecyclerListActivity.this);
                        }
                        return false;
                    }
                })
                .withSelectedItem(-1)
                .withSavedInstance(savedInstanceState)
                .build();


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_file_upload).color(Color.WHITE).actionBar());
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mAdapter = new ApplicationAdapter(new ArrayList<AppInfo>(), R.layout.row_application, RecyclerListActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        // Handle ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitializeApplicationsTask().execute();
            }
        });

        //show progress
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (drawer != null) {
            outState = drawer.saveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initData() {
        new InitializeApplicationsTask().execute();
    }

    @Override
    public void refresh() {
        super.refresh();
    }


    /**
     * A simple AsyncTask to load the list of applications and display them
     */
    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mAdapter.clearApplications();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            applicationList.clear();

            //Query the applications
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo ri : ril) {
                applicationList.add(new AppInfo(RecyclerListActivity.this, ri));
            }
            Collections.sort(applicationList);

            for (AppInfo appInfo : applicationList) {
                //load icons before shown. so the list is smoother
                appInfo.getIcon();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //handle visibility
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            //set data for list
            mAdapter.addApplications(applicationList);
            mSwipeRefreshLayout.setRefreshing(false);

            super.onPostExecute(result);
        }
    }

    /**
     * helper class to start the new detailActivity animated
     *
     * @param appInfo
     * @param appIcon
     */
    public void animateActivity(AppInfo appInfo, View appIcon,View appTitle) {
        Intent i = new Intent(this, RecyclerListDetailActivity.class);
        i.putExtra("appInfo", appInfo.getComponentName());
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create((View) mToolbar, "toolbar"),
                Pair.create((View) mFab, "fab"),
                Pair.create(appIcon, "appIcon"),
                Pair.create(appTitle, "appTitle"));
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
