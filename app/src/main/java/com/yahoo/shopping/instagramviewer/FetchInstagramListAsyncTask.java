package com.yahoo.shopping.instagramviewer;

import android.os.AsyncTask;

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

    public FetchInstagramListAsyncTask(OnListContentFetchComplete callBack) {
        this.mCallback = callBack;
    }

    private String fetchContent() throws IOException {
        URL url = new URL("https://api.instagram.com/v1/tags/sea/media/recent?access_token=2023497168.f98fd96.614b101c66254428b8e37124edd54710");
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
            model.setCreateTIme(new Date(item.getInt("created_time") * 1000));
            model.setImageUrl(item.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
            model.setProfilePicUrl(item.getJSONObject("user").getString("profile_picture"));
            model.setUserName(item.getJSONObject("user").getString("username"));
            model.setTitle(item.getJSONObject("caption").getString("text"));

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
        mCallback.onListContentFetchComplete(list);
    }
}
