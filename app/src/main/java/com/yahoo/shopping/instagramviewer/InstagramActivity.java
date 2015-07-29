package com.yahoo.shopping.instagramviewer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class InstagramActivity extends ActionBarActivity implements OnListContentFetchComplete, SwipeRefreshLayout.OnRefreshListener {
    public static final String INSTAGRAM_ACTIVITY_PREF = "INSTAGRAM_ACTIVITY_PREF";
    public static final String INSTAGRAM_ACTIVITY_PREF_ACCESS_TOKEN = "INSTAGRAM_ACTIVITY_PREF_ACCESS_TOKEN";

    private List<InstagramModel> mPhotoList = new ArrayList<>();
    private InstagramListAdapter mInstagramListAdapter;
    private FetchInstagramListAsyncTask fetchInstagramListAsyncTask;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        mInstagramListAdapter = new InstagramListAdapter(this, 0, mPhotoList);

        ListView lvPhotoList = (ListView) findViewById(R.id.activity_instagram_lv_photo_list);
        lvPhotoList.setAdapter(mInstagramListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_instagram_ly_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // put
        SharedPreferences prefs = getSharedPreferences(INSTAGRAM_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(INSTAGRAM_ACTIVITY_PREF_ACCESS_TOKEN, "2023497168.f98fd96.614b101c66254428b8e37124edd54710");
        editor.commit();

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
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
    }

    @Override
    public void onListContentFetchComplete(List<InstagramModel> list) {
        if (list != null) {
            mPhotoList.clear();
            mPhotoList.addAll(list);
        }

        mInstagramListAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void refreshList() {
        if (fetchInstagramListAsyncTask != null) {
            fetchInstagramListAsyncTask.cancel(true);
        }

        fetchInstagramListAsyncTask = new FetchInstagramListAsyncTask(this, this);
        fetchInstagramListAsyncTask.execute();
    }

    @Override
    public void onRefresh() {
        refreshList();
    }
}
