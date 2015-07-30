package com.yahoo.shopping.instagramviewer.asynctasks;

import com.yahoo.shopping.instagramviewer.InstagramModel;

import java.util.List;

/**
 * Created by jamesyan on 7/28/15.
 */
public interface OnListContentFetchComplete {
    public void onListContentFetchComplete(List<InstagramModel> list);
}
