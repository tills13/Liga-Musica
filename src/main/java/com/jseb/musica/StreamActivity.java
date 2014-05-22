package com.jseb.musica;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Session;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.jseb.musica.adapters.StreamAdapter;
import com.jseb.musica.callbacks.OnRequestCompleteCallback;
import com.jseb.musica.constants.Constants;
import com.jseb.musica.listeners.ScrollListener;

/**
 * Created by Tyler Sebastian on 10/24/13.
 */
public class StreamActivity extends YouTubeBaseActivity {
	private static final String TAG = "com.jseb.musica.StreamActivity";
	public static String paging_next;
	private ListView mListView;
	public StreamAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream_activity);
		paging_next = null;

		mListView = (ListView) findViewById(R.id.stream);
		mAdapter = new StreamAdapter(this, true);

		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		progressBar.setIndeterminate(true);
		mListView.setEmptyView(progressBar);
		((ViewGroup) findViewById(android.R.id.content)).addView(progressBar);
		View blank = getLayoutInflater().inflate(R.layout.headerfooter, null);
		mListView.addHeaderView(blank);
		mListView.addFooterView(blank);
		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(new ScrollListener(mAdapter));
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				PostActivity.mPost = mAdapter.getItem(i - 1);
				Intent intent = new Intent(StreamActivity.this, PostActivity.class);
				startActivity(intent);
			}
		});

		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stream, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.logout:
				MainActivity.logout();
				finish();
			case R.id.refresh:
				refresh();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void scrollToPost(int pos) {
		mListView.smoothScrollToPosition(pos);
	}

	public void refresh() {
		mAdapter.clearData();
		paging_next = null;
		Session session = Session.getActiveSession();
		Request.newGraphPathRequest(session, Constants.GROUP_ID + Constants.GRAPH_URL_FEED, new OnRequestCompleteCallback(this.mAdapter, Constants.TYPE_POSTS_CALLBACK)).executeAsync();
	}

	public StreamAdapter getAdapter() {
		return this.mAdapter;
	}
}
