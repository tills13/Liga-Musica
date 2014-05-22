package com.jseb.musica.callbacks;

import android.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.jseb.musica.PostActivity;
import com.jseb.musica.R;
import com.jseb.musica.constants.Constants;
import com.jseb.musica.objects.Post;

import org.json.JSONException;
import org.json.JSONObject;

public class PostActivityOnCompleteCallback implements Request.Callback {
	private PostActivity mContext;
	private Post mPost;

	public PostActivityOnCompleteCallback(PostActivity context, Post post) {
		this.mContext = context;
		this.mPost = post;
	}

	@Override
	public void onCompleted(Response response) {
		//final TextView description = (TextView) mContext.findViewById(R.id.description);
		final TextView switcher = (TextView) mContext.findViewById(R.id.post_switcher);
		TextView num_likes = (TextView) mContext.findViewById(R.id.post_like);
		TextView num_comments = (TextView) mContext.findViewById(R.id.post_comment);

		//description.setText(mPost.getContent());
		switcher.setOnClickListener(new View.OnClickListener() {
			int state = 0;
			@Override
			public void onClick(View view) {
				if (state == 0) {
					state = 1;
					//description.setVisibility(View.VISIBLE);
					//description.bringToFront();
					//mContext.findViewById(R.id.youtube_view).setVisibility(View.GONE);
					switcher.setText("preview");
				} else {
					state = 0;
					//description.setVisibility(View.GONE);
					//mContext.findViewById(R.id.youtube_view).setVisibility(View.GONE);
					switcher.setText("description");
				}
			}
		});

		try {
			final GraphObject post = response.getGraphObject();
			JSONObject post_json = post.getInnerJSONObject();

			ActionBar ab = mContext.getActionBar();
			switch (mPost.getType()) {
				case Constants.TYPE_VIDEO:
					ab.setTitle(post_json.getString("name"));
					break;
				case Constants.TYPE_TEXT:
					ab.setTitle(post_json.getJSONObject("to").getJSONArray("data").getJSONObject(0).getString("name"));
					break;
			}

			ab.setSubtitle(post_json.getJSONObject("from").getString("name"));
			ab.setDisplayHomeAsUpEnabled(true);

			num_likes.setText(String.format(mContext.getString(R.string.post_like), post_json.has("likes") ? post_json.getJSONObject("likes").getJSONArray("data").length() : 0));

			num_likes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					Request.newPostRequest(Session.getActiveSession(), (Constants.GRAPH_URL_FEED + "_" + mPost.getId() + "/likes"), null, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							mPost.like();
							((TextView) view).setTextColor(android.R.color.holo_blue_dark);
						}
					}).executeAsync();
				}
			});
		} catch (JSONException e) {
			Toast.makeText(mContext, "something went wrong", Toast.LENGTH_LONG).show();
		}
	}
}
