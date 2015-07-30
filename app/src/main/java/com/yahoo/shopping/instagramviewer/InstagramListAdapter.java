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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yahoo.shopping.instagramviewer.asynctasks.FetchImageAsyncTask;
import com.yahoo.shopping.instagramviewer.comment.Comment;
import com.yahoo.shopping.instagramviewer.comment.InstagramCommentAdapter;
import com.yahoo.shopping.instagramviewer.view.RoundedImageView;

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
            viewHolder.tvCaptionUserName = (TextView) convertView.findViewById(R.id.activity_instagram_comment_list_view_tv_caption_user_name);
            viewHolder.tvCaptionComment = (TextView) convertView.findViewById(R.id.activity_instagram_comment_list_view_tv_caption_comment);
            viewHolder.tvLikeCounts = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_like_counts);
            viewHolder.tvCommentCounts = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_comment_counts);
            viewHolder.tvCreateTime = (TextView) convertView.findViewById(R.id.activity_instagram_list_view_tv_create_date);
            viewHolder.lyCommentList = (LinearLayout) convertView.findViewById(R.id.activity_instagram_list_view_ly_comment_list);

            convertView.setTag(R.id.activity_instagram_item_list_view_holder, viewHolder);
        }

        InstagramModel model = mPhotoList.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag(R.id.activity_instagram_item_list_view_holder);
        AsyncTask<String, Void, Bitmap> profileImageAsyncTask = (AsyncTask<String, Void, Bitmap>) convertView.getTag(R.id.activity_instagram_item_list_profile_async_task);
        AsyncTask<String, Void, Bitmap> imageAyncTask = (AsyncTask<String, Void, Bitmap>) convertView.getTag(R.id.activity_instagram_item_list_photo_async_task);

        if (model != null) {
            viewHolder.tvUserName.setText(model.getUserName());
            viewHolder.tvCaptionUserName.setText(model.getUserName());
            viewHolder.tvCaptionComment.setText(model.getTitle());
            viewHolder.ivPhoto.setImageBitmap(null);
            viewHolder.ivProfilePhoto.setImageBitmap(null);
            viewHolder.tvLikeCounts.setText(model.getLikeCounts() + " Likes");
            viewHolder.tvCommentCounts.setText(model.getComments().size() + " Comments");
            viewHolder.tvCreateTime.setText(new PrettyTime().format(model.getCreateTIme()));

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (model.getComments().size() > 3) {
                viewHolder.lyCommentList.removeAllViews();

                for(Comment comment: model.getComments().subList(0, 2)) {
                    View view = inflater.inflate(R.layout.activity_instagram_comment_list_view, null);

                    RoundedImageView ivProfile = (RoundedImageView) view.findViewById(R.id.activity_instagram_comment_list_view_iv_profile_photo);
                    TextView tvCommentUserName = (TextView) view.findViewById(R.id.activity_instagram_comment_list_view_tv_user_name);
                    TextView tvCommentComment = (TextView) view.findViewById(R.id.activity_instagram_comment_list_view_tv_comment);

                    tvCommentUserName.setText(comment.getUserName());
                    tvCommentComment.setText(comment.getComment());
                    new FetchImageAsyncTask(ivProfile).execute(comment.getProfilePicUrl());

                    viewHolder.lyCommentList.addView(view);
                }
            }

            if (profileImageAsyncTask != null) profileImageAsyncTask.cancel(true);
            if (imageAyncTask != null) imageAyncTask.cancel(true);

            profileImageAsyncTask = new FetchImageAsyncTask(viewHolder.ivProfilePhoto).execute(model.getProfilePhotoUrl());
            imageAyncTask = new FetchImageAsyncTask(viewHolder.ivPhoto).execute(model.getPhotoUrl());

            convertView.setTag(R.id.activity_instagram_item_list_profile_async_task, profileImageAsyncTask);
            convertView.setTag(R.id.activity_instagram_item_list_photo_async_task, imageAyncTask);
        }

        return convertView;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++){
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    private class ViewHolder {
        public ImageView ivProfilePhoto;
        public ImageView ivPhoto;
        public TextView tvUserName;
        public TextView tvCaptionUserName;
        public TextView tvCaptionComment;
        public TextView tvCreateTime;
        public TextView tvLikeCounts;
        public TextView tvCommentCounts;
        public LinearLayout lyCommentList;
    }
}
