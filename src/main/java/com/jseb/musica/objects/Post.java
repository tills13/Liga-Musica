package com.jseb.musica.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jseb.musica.R;
import com.jseb.musica.constants.Constants;

/**
 * Created by Tyler Sebastian on 10/24/13.
 */
public class Post  {
	private int mType;
	private String mId;
	private String mAuthor;
	private String mTitle;
	private String mContent;
	private String mURL;
	private int num_comments;
	private int num_likes;
	private boolean isPlaylist;
	private Drawable img_preview;

	public Post(int type, String id, String title, String author, String content, String url, int comments, int likes) {
		this.mType = type;
		this.mId = id;
		this.mTitle = title;
		this.mAuthor = author;
		this.mContent = content;
		this.mURL = url;
		this.num_comments = comments;
		this.num_likes = likes;
		this.img_preview = null;

		prepare();
	}

	public void prepare() {
		switch (this.mType) {
			case Constants.TYPE_VIDEO:
				if (mContent.contains("http:")) {
					int index = mContent.indexOf("http:");
					int next_space = Math.min(mContent.indexOf(" ", index), mContent.indexOf("\n", index));
					if (next_space == -1) next_space = mContent.length() - 1;
					mContent = mContent.substring(0, index).concat(mContent.substring(next_space + 1, mContent.length())).replaceAll("\n", " ");
				}

				if (mURL.contains("list=")) isPlaylist = true;
				else isPlaylist = false;

				break;
			case Constants.TYPE_TEXT:
				break;
			//case Constants.TYPE_DOCUMENT:
		}
	}

	public String getVideoId() {
		return mURL.substring(mURL.indexOf("v/") + 2, mURL.indexOf("?"));
	}

	public String getId() {
		return this.mId;
	}

	public String getContent() {
		return this.mContent;
	}

	public int getType() {
		return this.mType;
	}

	public boolean getIsPlaylist() {
		return this.isPlaylist;
	}

	public Drawable getPreviewDrawable() {
		return this.img_preview;
	}

	public void setImagePreview(Drawable drawable) {
		this.img_preview = drawable;
	}

	public int getLikes() {
		return this.num_likes;
	}

	public void like() {
		this.num_likes++;
	}

	public View getView(final Context mContext) {
		View cardview = null;

		switch (this.mType) {
			case Constants.TYPE_VIDEO:
				cardview = LayoutInflater.from(mContext).inflate(R.layout.video_post, null);
				final YouTubeThumbnailView preview = (YouTubeThumbnailView) cardview.findViewById(R.id.preview);

				if (img_preview != null) {
					preview.setImageDrawable(img_preview);
				} else {
					preview.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
						@Override
						public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
							loader.setVideo(getVideoId());
							loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
								@Override
								public void onThumbnailLoaded(YouTubeThumbnailView view, String s) {
									img_preview = view.getDrawable();
								}

								@Override
								public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

								}
							});
						}

						@Override
						public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult loader) {

						}
					});
				}

				((TextView) cardview.findViewById(R.id.post_title)).setText(this.mTitle);

				break;
			case Constants.TYPE_TEXT:
				cardview = LayoutInflater.from(mContext).inflate(R.layout.text_post, null);
				((TextView) cardview.findViewById(R.id.post_content)).setText(this.mContent);
		}

		((TextView) cardview.findViewById(R.id.post_info)).setText(this.mAuthor + " • " + this.num_likes + " like(s)" + " • " + this.num_comments + " comment(s)");

		return cardview;
	}
}
