package com.jseb.musica.callbacks;

import android.util.Log;
import android.widget.BaseAdapter;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import com.jseb.musica.PostActivity;
import com.jseb.musica.StreamActivity;
import com.jseb.musica.adapters.CommentAdapter;
import com.jseb.musica.adapters.StreamAdapter;
import com.jseb.musica.constants.Constants;
import com.jseb.musica.objects.Comment;
import com.jseb.musica.objects.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Sebastian on 11/29/13.
 */
public class OnRequestCompleteCallback implements Request.Callback {
	private static String TAG = "com.jseb.musica.callbacks.OnRequestCompleteCallback";
	private BaseAdapter mAdapter;
	private int mType;

	public OnRequestCompleteCallback(BaseAdapter adapter, int type) {
		this.mAdapter = adapter;
		this.mType = type;
	}

	@Override
	public void onCompleted(Response response) {
		switch (this.mType) {
			case Constants.TYPE_POSTS_CALLBACK:
				List<Post> posts = new ArrayList<Post>();

				try {
					GraphObject graphObject = response.getGraphObject();
					if (graphObject != null) {
						JSONObject jsonObject = graphObject.getInnerJSONObject();
						StreamActivity.paging_next = jsonObject.getJSONObject("paging").getString("next");
						StreamActivity.paging_next = StreamActivity.paging_next.substring(StreamActivity.paging_next.lastIndexOf("?"));
						JSONArray jsonArray = jsonObject.getJSONArray("data");

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject post = (JSONObject) jsonArray.get(i);

							String id = post.getString("id");
							id = id.substring(id.indexOf("_") + 1, id.length());
							String author = post.getJSONObject("from").getString("name");
							String title = post.has("name") ? post.getString("name") : "No Title";
							String content = post.getString("message");
							int type = (post.has("source")) ? Constants.TYPE_VIDEO : Constants.TYPE_TEXT;
							String url = (post.has("source")) ? post.getString("source") : "";
							int num_comments = post.has("comments") ? post.getJSONObject("comments").getJSONArray("data").length() : 0;
							int num_likes = post.has("likes") ? post.getJSONObject("likes").getJSONArray("data").length() : 0;

							posts.add(new Post(type, id, title, author, content, url, num_comments, num_likes));
						}

					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
				}

				((StreamAdapter) mAdapter).addItems(posts);
				break;
			case Constants.TYPE_COMMENTS_CALLBACK:
				List<Comment> comments = new ArrayList<Comment>();

				try {
					GraphObject graphObject = response.getGraphObject();
					if (graphObject != null) {
						JSONObject jsonObject = graphObject.getInnerJSONObject();
						PostActivity.paging_next = jsonObject.getJSONObject("paging").getString("next");
						PostActivity.paging_next = PostActivity.paging_next.substring(PostActivity.paging_next.lastIndexOf("?"));
						JSONArray jsonArray = jsonObject.getJSONArray("data");

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject post = (JSONObject) jsonArray.get(i);

							String id = post.getString("id");
							id = id.substring(id.indexOf("_") + 1, id.length());
							String author = post.getJSONObject("from").getString("name");
							String content = post.getString("message");
							int num_likes = post.has("likes") ? post.getJSONObject("likes").getJSONArray("data").length() : 0;

							comments.add(new Comment(id, author, content, num_likes));
						}

					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
				}

				((CommentAdapter) mAdapter).addItems(comments);
		}

	}
}
