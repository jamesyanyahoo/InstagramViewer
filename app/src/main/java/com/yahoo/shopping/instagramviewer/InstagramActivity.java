package com.yahoo.shopping.instagramviewer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class InstagramActivity extends ActionBarActivity implements OnListContentFetchComplete{
    private List<InstagramModel> mPhotoList = new ArrayList<>();
    private InstagramListAdapter mInstagramListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        mInstagramListAdapter = new InstagramListAdapter(this, 0, mPhotoList);

        ListView lvPhotoList = (ListView) findViewById(R.id.activity_instagram_lv_photo_list);
        lvPhotoList.setAdapter(mInstagramListAdapter);

        FetchInstagramListAsyncTask task = new FetchInstagramListAsyncTask(this);
        task.execute();
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
            mPhotoList.addAll(list);
        }

        mInstagramListAdapter.notifyDataSetChanged();
    }
}
