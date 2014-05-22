package com.jseb.musica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jseb.musica.R;
import com.jseb.musica.objects.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Sebastian on 10/26/13.
 */
public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Comment> comments;

	public CommentAdapter(Context context) {
		this.mContext = context;
		this.comments = new ArrayList<Comment>();
	}

	public void setData(ArrayList<Comment> comments) {
		this.comments = comments;
		notifyDataSetChanged();
	}

	public void addItems(List<Comment> comments) {
		this.comments.addAll(comments);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Comment getItem(int pos) {
		return comments.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.post_comment, null);

		TextView message = (TextView) view.findViewById(R.id.comment_message);
		TextView author = (TextView) view.findViewById(R.id.comment_author_and_likes);

		message.setText(getItem(pos).message);
		author.setText(getItem(pos).author + " • " + getItem(pos).num_likes + " like(s)");

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// show dialog for like
			}
		});

		return view;
	}
}
