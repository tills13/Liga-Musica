package com.jseb.musica.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jseb.musica.objects.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Sebastian on 10/24/13.
 */
public class StreamAdapter extends BaseAdapter {
	public Context mContext;
	public boolean mSwipeable;
	public ArrayList<Post> posts;

	public StreamAdapter(Context context) {
		this(context, false);
	}

	public StreamAdapter(Context context, boolean swipeable) {
		this.mContext = context;
		this.mSwipeable = swipeable;
		posts = new ArrayList<Post>();
	}

	public void setData(ArrayList<Post> posts) {
		this.posts = posts;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return posts.size();
	}

	public Post getItemById(String id) {
		for (Post post : posts) {
			if ((post.getId().equalsIgnoreCase(id))) return post;
		}

		return null;
	}

	@Override
	public Post getItem(int pos) {
		return posts.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	public void addItem(Post post) {
		this.posts.add(post);
		notifyDataSetChanged();
	}

	public void addItems(List<Post> items) {
		posts.addAll(items);
		notifyDataSetChanged();
	}

	public void clearData() {
		posts.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		return getItem(pos).getView(mContext);
	}
}
