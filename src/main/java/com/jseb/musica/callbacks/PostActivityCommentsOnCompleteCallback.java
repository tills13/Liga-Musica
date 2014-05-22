package com.jseb.musica.callbacks;

import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import com.jseb.musica.PostActivity;
import com.jseb.musica.R;
import com.jseb.musica.objects.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostActivityCommentsOnCompleteCallback implements Request.Callback {
	private PostActivity mContext;

	public PostActivityCommentsOnCompleteCallback(PostActivity context) {
		this.mContext = context;
	}

	@Override
	public void onCompleted(Response response) {
		ArrayList<Comment> com_list = new ArrayList<Comment>();
		try {
			GraphObject post = response.getGraphObject();
			JSONObject post_json = post.getInnerJSONObject();

			((TextView) mContext.findViewById(R.id.post_comment)).setText(String.format(mContext.getString(R.string.post_comment),post_json.getJSONArray("data").length()));


			JSONArray comment_json = post_json.getJSONArray("data");

			for (int i = 0; i < comment_json.length(); i++) {
				JSONObject com = comment_json.getJSONObject(i);

				String id = com.getString("id");
				String author = com.getJSONObject("from").getString("name");
				String message = com.getString("message");
				int likes = com.getInt("like_count");

				com_list.add(new Comment(id, author, message, likes));
			}
		} catch (JSONException e) {
			Toast.makeText(mContext, "something went wrong", Toast.LENGTH_LONG).show();
		}

		mContext.getAdapter().setData(com_list);
	}
}
