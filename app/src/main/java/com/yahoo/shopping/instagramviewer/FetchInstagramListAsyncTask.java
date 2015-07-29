package com.yahoo.shopping.instagramviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jamesyan on 7/28/15.
 */
public class FetchInstagramListAsyncTask extends AsyncTask<Void, Void, List<InstagramModel>> {
    private static final String TAG = "FetchInstagramList";

    private OnListContentFetchComplete mCallback;
    private boolean mCancelOperation = false;
    private Context mContext;

    public FetchInstagramListAsyncTask(OnListContentFetchComplete callBack, Context context) {
        this.mCallback = callBack;
        this.mContext = context;
    }

    private String fetchContent() throws IOException {
        SharedPreferences prefs = mContext.getSharedPreferences(InstagramActivity.INSTAGRAM_ACTIVITY_PREF, Context.MODE_PRIVATE);
        String apiKey = prefs.getString(InstagramActivity.INSTAGRAM_ACTIVITY_PREF_ACCESS_TOKEN, null);
        if (apiKey == null) {
            Log.i(TAG, "can not fetch api key");
            return null;
        }

//        URL url = new URL("https://api.instagram.com/v1/tags/sea/media/recent?access_token=" + apiKey);
        URL url = new URL("https://api.instagram.com/v1/media/popular?access_token=" + apiKey);
        URLConnection urlConnection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        StringBuffer jsonString = new StringBuffer();
        String line;
        do {
            line = reader.readLine();

            if (line != null) {
                jsonString.append(line);
            }
        } while (line != null);

        return jsonString.toString();
    }

    private List<InstagramModel> convertToInstagramModelList(String jsonString) throws JSONException {
        List<InstagramModel> list = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray data = root.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            InstagramModel model = new InstagramModel();

            JSONObject item = data.getJSONObject(i);
            model.setLink(item.getString("link"));
            model.setCreateTIme(new Date(item.getLong("created_time") * 1000));

            if (! item.isNull("images") && ! item.getJSONObject("images").isNull("standard_resolution")) {
                model.setImageUrl(item.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
            }

            if (! item.isNull("user")) {
                model.setProfilePicUrl(item.getJSONObject("user").getString("profile_picture"));
                model.setUserName(item.getJSONObject("user").getString("username"));
            }

            if (! item.isNull("caption")) {
                model.setTitle(item.getJSONObject("caption").getString("text"));
            }

            if (! item.isNull("likes")) {
                model.setLikeCounts(item.getJSONObject("likes").getInt("count"));
            }

            if (! item.isNull("comments")) {
                JSONArray commentsItem = item.getJSONObject("comments").getJSONArray("data");
                List<Comment> commentList = new ArrayList<>();
                for (int j = 0; j < commentsItem.length(); j++) {
                    JSONObject commentObj = commentsItem.getJSONObject(j);

                    if (! commentObj.isNull("from") && ! commentObj.isNull("text")) {
                        commentList.add(new Comment(commentObj.getJSONObject("from").getString("username"),
                                commentObj.getJSONObject("from").getString("profile_picture"),
                                commentObj.getString("text")));
                    }
                }
                model.setComments(commentList);
            }

            list.add(model);
        }

        return list;
    }

    @Override
    protected List<InstagramModel> doInBackground(Void... params) {
        try {
            String jsonString = fetchContent();
            return convertToInstagramModelList(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<InstagramModel> list) {
        if (mCancelOperation == false) {
            mCallback.onListContentFetchComplete(list);
        }
    }

    @Override
    protected void onCancelled() {
        mCancelOperation = true;
    }
}
