package com.jseb.musica.objects;

/**
 * Created by Tyler Sebastian on 10/26/13.
 */
public class Comment {
	public String id;
	public String message;
	public String author;
	public int num_likes;

	public Comment(String id, String author, String message, int likes) {
		this.id = id;
		this.author = author;
		this.message = message;
		this.num_likes = likes;
	}
}
