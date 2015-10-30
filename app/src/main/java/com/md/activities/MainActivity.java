package com.md.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.md.R;
import com.md.helper.json_xml_paser.utils.XmlPullParserUtil;
import com.md.helper.utils.L;
import com.md.imp.IOperate;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActionBarActivity {

    private ListView ls;
    private SwipeRefreshLayout sw;
    private SimpleAdapter simpleAdapter;

    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    private ProgressDialog progressDialog;

    private List<Map<String, String>>  components = new ArrayList<Map<String, String>>();

    private Toolbar mToolbar;

    private FloatingActionButton mFab;


    CollapsingToolbarLayout collapsingToolbarLayout;

    CoordinatorLayout rootLayout;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void initView() {

        //顶部工具条
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 顶部工具条title
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Material Design");

        // 侧拉框
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.action_settings, R.string.action_settings);
        drawerLayout.setDrawerListener(drawerToggle);

        //下拉刷新
        sw = (SwipeRefreshLayout) findViewById(R.id.sw);
        sw.setColorSchemeResources(R.color.color_scheme_1_1, R.color.color_scheme_1_2,
                R.color.color_scheme_1_3, R.color.color_scheme_1_4);

        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       refresh();
                    }
                }, 2000);
            }
        });

        // 列表
        ls = (ListView) findViewById(R.id.list_view);

        simpleAdapter = new SimpleAdapter(this, components, R.layout.listview_all_item, new String[]{"title"}, new int[]{R.id.name});

        ls.setAdapter(simpleAdapter);


        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> item = (Map<String,String>)(parent.getItemAtPosition(position));
                if(item!=null){
                    L.i(TAG, item.get("title"));
                    try {
                      Intent i =  new Intent(MainActivity.this, Class.forName(item.get("actionName").trim()));
                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,Pair.create((View) mToolbar, "toolbar"),Pair.create((View) mFab, "fab"));
                        startActivityForResult(i,0,transitionActivityOptions.toBundle());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //悬浮按钮
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home).color(Color.WHITE).actionBar());
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });




    }

    @Override
    public void initData() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        new ParseComponentXmlAsynTask().execute();
    }

    @Override
    public void refresh() {
        new ParseComponentXmlAsynTask().execute();
    }


    /***
     * 解析  components.xml  返回
     *
     * @return List<Map<String, String>>
     */
    class ParseComponentXmlAsynTask extends AsyncTask<Void, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            List<Map<String, String>> datas = new ArrayList<>();

            InputStream in = null;
            try {
                in = getResources().getAssets().open("components.xml");
                return XmlPullParserUtil.pullParser(in);
            } catch (IOException e) {
                e.printStackTrace();
                L.e(TAG, "components.xml error");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return datas;
        }

        protected void onPostExecute( List<Map<String, String>> result) {
            components.clear();
            components.addAll(result);
            simpleAdapter.notifyDataSetChanged();
            if(sw!=null){
                sw.setRefreshing(false);
            }
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
