package com.yahoo.shopping.instagramviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jamesyan on 7/29/15.
 */
public class InstagramCommentAdapter extends ArrayAdapter<Comment> {
    List<Comment> mCommentList;
    Context mContext;

    public InstagramCommentAdapter(Context context, int resource, int textViewResourceId, List<Comment> objects) {
        super(context, resource, textViewResourceId, objects);

        mContext = context;
        mCommentList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_instagram_comment_list_view, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivProfilePhoto = (RoundedImageView) convertView.findViewById(R.id.activity_instagram_comment_list_view_iv_profile_photo);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.activity_instagram_comment_list_view_tv_user_name);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.activity_instagram_comment_list_view_tv_comment);

            convertView.setTag(R.id.activity_instagram_item_list_view_holder, viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag(R.id.activity_instagram_item_list_view_holder);
        AsyncTask<String, Void, Bitmap> profileImageAsyncTask = (AsyncTask<String, Void, Bitmap>) convertView.getTag(R.id.activity_instagram_item_list_profile_async_task);

        Comment comment = mCommentList.get(position);
        if (comment != null) {
            viewHolder.tvUserName.setText(comment.getUserName());
            viewHolder.tvComment.setText(comment.getComment());

            if (profileImageAsyncTask != null) {
                profileImageAsyncTask.cancel(true);
            }

            profileImageAsyncTask = new FetchImageAsyncTask(viewHolder.ivProfilePhoto).execute(comment.getProfilePicUrl());
            convertView.setTag(R.id.activity_instagram_item_list_profile_async_task, profileImageAsyncTask);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView ivProfilePhoto;
        public TextView tvUserName;
        public TextView tvComment;
    }

}
