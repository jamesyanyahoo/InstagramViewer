package com.yahoo.shopping.instagramviewer.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jamesyan on 7/28/15.
 */
public class FetchImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "InstagramListAdapter";

    private ImageView mView;
    private boolean mCancelRender = false;

    public FetchImageAsyncTask(ImageView view) {
        mView = view;
    }

    private Bitmap fetchImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.i(TAG, "can not fetch url: " + url);
        }
        return bm;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params[0] != null) {
            return fetchImageBitmap(params[0]);
        } else {
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        mCancelRender = true;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mCancelRender) {
            return;
        }

        mView.setImageBitmap(bitmap);
    }
}
