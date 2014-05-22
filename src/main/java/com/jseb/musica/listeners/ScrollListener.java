package com.jseb.musica.listeners;

import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.facebook.Request;
import com.facebook.Session;
import com.jseb.musica.PostActivity;
import com.jseb.musica.StreamActivity;
import com.jseb.musica.adapters.CommentAdapter;
import com.jseb.musica.callbacks.OnRequestCompleteCallback;
import com.jseb.musica.constants.Constants;

/**
 * Created by Tyler Sebastian on 11/29/13.
 */
public class ScrollListener implements AbsListView.OnScrollListener {
	private boolean loading;
	private BaseAdapter mAdapter;
	private int previousTotal = 0;

	public ScrollListener(BaseAdapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public void onScrollStateChanged(AbsListView absListView, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
		}

		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 5)) {
			if (totalItemCount - 2 == 0) return; // headers count
			String graphPath = ((this.mAdapter instanceof CommentAdapter) ? PostActivity.mPost.getId() +  Constants.GRAPH_URL_COMMENTS : Constants.GROUP_ID + Constants.GRAPH_URL_FEED);
			graphPath += ((this.mAdapter instanceof CommentAdapter)) ? (PostActivity.paging_next == null) ? "" : PostActivity.paging_next : (StreamActivity.paging_next == null) ? "" : StreamActivity.paging_next;
			OnRequestCompleteCallback callback = new OnRequestCompleteCallback(this.mAdapter, (this.mAdapter instanceof CommentAdapter) ? Constants.TYPE_COMMENTS_CALLBACK : Constants.TYPE_POSTS_CALLBACK);
			Request.newGraphPathRequest(Session.getActiveSession(), graphPath, callback).executeAsync();
			loading = true;
		}
	}
}
