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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

/**
 * Created by jamesyan on 7/28/15.
 */
public class InstagramListAdapter extends ArrayAdapter<InstagramModel> {
    private static final String TAG = "InstagramListAdapter";

    private List<InstagramModel> mPhotoList;
    private Context mContext;

    public InstagramListAdapter(Context context, int resource, List<InstagramModel> objects) {
        super(context, resource, objects);

        mContext = context;
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
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_txt_profile_name);
            viewHolder.tvLikeCounts = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_like_counts);
            viewHolder.tvCommentCounts = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_comment_counts);
            viewHolder.tvCreateTime = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_create_date);
            viewHolder.lvCommentList = (ListView) convertView.findViewById(R.id.activity_instagram_list_view_lv_comment_list);

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
            viewHolder.tvLikeCounts.setText(model.getLikeCounts() + " Likes");
            viewHolder.tvCommentCounts.setText(model.getComments().size() + " Comments");
            viewHolder.tvCreateTime.setText(new PrettyTime().format(model.getCreateTIme()));

            if (model.getComments().size() > 3) {
                viewHolder.lvCommentList.setAdapter(new InstagramCommentAdapter(mContext, 0, 0, model.getComments().subList(0, 2)));
                setListViewHeightBasedOnChildren(viewHolder.lvCommentList);
            }

            if (profileImageAsyncTask != null) profileImageAsyncTask.cancel(true);
            if (imageAyncTask != null) imageAyncTask.cancel(true);

            profileImageAsyncTask = new FetchImageAsyncTask(viewHolder.ivProfilePhoto).execute(model.getProfilePicUrl());
            imageAyncTask = new FetchImageAsyncTask(viewHolder.ivPhoto).execute(model.getImageUrl());

            convertView.setTag(R.id.activity_instagram_item_list_profile_async_task, profileImageAsyncTask);
            convertView.setTag(R.id.activity_instagram_item_list_photo_async_task, imageAyncTask);
        }

        return convertView;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private class ViewHolder {
        public ImageView ivProfilePhoto;
        public ImageView ivPhoto;
        public TextView tvUserName;
        public TextView tvCreateTime;
        public TextView tvLikeCounts;
        public TextView tvCommentCounts;
        public ListView lvCommentList;
    }
}
