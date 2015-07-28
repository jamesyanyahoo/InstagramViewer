package com.yahoo.shopping.instagramviewer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jamesyan on 7/28/15.
 */
public class InstagramListAdapter extends ArrayAdapter<InstagramModel> {
    private static final String TAG = "InstagramListAdapter";

    private List<InstagramModel> mPhotoList;

    public InstagramListAdapter(Context context, int resource, List<InstagramModel> objects) {
        super(context, resource, objects);
        mPhotoList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_instagram_list_view, null);
        }

        ImageView ivProfilePhoto = (ImageView) convertView.findViewById(R.id.activity_instagram_list_view_iv_profile_photo);
//        GifView ivPhoto = (GifView) convertView.findViewById(R.id.activity_instagram_list_view_iv_photo);
//        TextView txtPhotoTitle = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_txt_photo_title);
        TextView txtUserName = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_txt_profile_name);

        InstagramModel model = mPhotoList.get(position);
        if (model != null) {
//            txtPhotoTitle.setText(model.getTitle());
            txtUserName.setText(model.getUserName());

            new FetchImageAsyncTask(ivProfilePhoto).execute(model.getProfilePicUrl());
//            new FetchImageAsyncTask(ivPhoto).execute(model.getImageUrl());
        }

        return convertView;
    }
}
