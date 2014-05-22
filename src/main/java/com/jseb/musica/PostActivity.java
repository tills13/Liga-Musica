package com.jseb.musica;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Session;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jseb.musica.adapters.CommentAdapter;
import com.jseb.musica.callbacks.OnRequestCompleteCallback;
import com.jseb.musica.callbacks.PostActivityOnCompleteCallback;
import com.jseb.musica.constants.Constants;
import com.jseb.musica.listeners.ScrollListener;
import com.jseb.musica.objects.Post;

/**
 * Created by Tyler Sebastian on 10/25/13.
 */
public class PostActivity extends YouTubeBaseActivity {
	public static Post mPost;
	public static String paging_next;
	private CommentAdapter mCommentsAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (mPost.getType()) {
			case Constants.TYPE_VIDEO:
				setContentView(R.layout.post_activity_video);
				break;
			case Constants.TYPE_TEXT:
				setContentView(R.layout.post_activity_text);
				break;
		}

		mListView = (ListView) findViewById(R.id.comments);
		mCommentsAdapter = new CommentAdapter(this);

		View blank = getLayoutInflater().inflate(R.layout.headerfooter, null);
		mListView.addHeaderView(blank);
		mListView.addFooterView(blank);
		mListView.setAdapter(mCommentsAdapter);
		mListView.setOnScrollListener(new ScrollListener(mCommentsAdapter));

		loadData();
		loadComments();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.post_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public CommentAdapter getAdapter() {
		return this.mCommentsAdapter;
	}

	public void loadComments() {
		Request.newGraphPathRequest(Session.getActiveSession(), mPost.getId() + Constants.GRAPH_URL_COMMENTS, new OnRequestCompleteCallback(mCommentsAdapter, Constants.TYPE_COMMENTS_CALLBACK)).executeAsync();
	}

	public void loadData() {
		Request.newGraphPathRequest(Session.getActiveSession(), mPost.getId(), new PostActivityOnCompleteCallback(this, mPost)).executeAsync();

		switch (mPost.getType()) {
			case Constants.TYPE_VIDEO:
				((YouTubePlayerView) findViewById(R.id.youtube_view)).initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
					@Override
					public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
						youTubePlayer.setShowFullscreenButton(true);
						if (!mPost.getIsPlaylist()) youTubePlayer.loadVideo(mPost.getVideoId());
						else youTubePlayer.loadPlaylist(mPost.getVideoId());
					}

					@Override
					public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

					}
				});

				break;
			case Constants.TYPE_TEXT:
				((TextView) findViewById(R.id.post_content)).setText(mPost.getContent());
				break;
		}
	}
}
