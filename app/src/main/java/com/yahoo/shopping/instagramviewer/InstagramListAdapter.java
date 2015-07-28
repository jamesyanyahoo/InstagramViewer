package com.yahoo.shopping.instagramviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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

            // ViewHolder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.activity_instagram_list_view_iv_photo);
            viewHolder.ivProfilePhoto = (ImageView) convertView.findViewById(R.id.activity_instagram_list_view_iv_profile_photo);
//            viewHolder.ivPhotoLoading = (ImageView) convertView.findViewById(R.id.activity_instagram_list_view_iv_photo_loading);
//            viewHolder.ivProfilePhotoLoading = (ImageView) convertView.findViewById(R.id.activity_instagram_list_view_iv_profile_photo_loading);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_txt_profile_name);

            convertView.setTag(R.id.activity_instagram_item_list_view_holder, viewHolder);
        }

        InstagramModel model = mPhotoList.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag(R.id.activity_instagram_item_list_view_holder);
        AsyncTask<String, Void, Bitmap> profileImageAsyncTask = (AsyncTask<String, Void, Bitmap>) convertView.getTag(R.id.activity_instagram_item_list_profile_async_task);
        AsyncTask<String, Void, Bitmap> imageAyncTask = (AsyncTask<String, Void, Bitmap>) convertView.getTag(R.id.activity_instagram_item_list_photo_async_task);

        if (model != null) {
            viewHolder.tvUserName.setText(model.getUserName());
            viewHolder.ivPhoto.setImageBitmap(null);
            viewHolder.ivProfilePhoto.setImageBitmap(null);
//            viewHolder.ivPhotoLoading.setVisibility(View.VISIBLE);
//            viewHolder.ivProfilePhotoLoading.setVisibility(View.VISIBLE);

            if (profileImageAsyncTask != null) profileImageAsyncTask.cancel(true);
            if (imageAyncTask != null) imageAyncTask.cancel(true);

//            profileImageAsyncTask = new FetchImageAsyncTask(viewHolder.ivProfilePhoto, viewHolder.ivProfilePhotoLoading).execute(model.getProfilePicUrl());
//            imageAyncTask = new FetchImageAsyncTask(viewHolder.ivPhoto, viewHolder.ivPhotoLoading).execute(model.getImageUrl());
            profileImageAsyncTask = new FetchImageAsyncTask(viewHolder.ivProfilePhoto).execute(model.getProfilePicUrl());
            imageAyncTask = new FetchImageAsyncTask(viewHolder.ivPhoto).execute(model.getImageUrl());

            convertView.setTag(R.id.activity_instagram_item_list_profile_async_task, profileImageAsyncTask);
            convertView.setTag(R.id.activity_instagram_item_list_photo_async_task, imageAyncTask);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView ivProfilePhoto;
        public ImageView ivPhoto;
//        public ImageView ivPhotoLoading;
//        public ImageView ivProfilePhotoLoading;
        public TextView tvUserName;
    }
}
