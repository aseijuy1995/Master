package nom.cp101.master.master.ExperienceArticleActivity;

import java.util.Date;

public class Comment {
	int comment_id, post_id;
	String user_id, comment_content;
	Date comment_time;

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public Date getComment_time() {
		return comment_time;
	}

	public void setComment_time(Date comment_time) {
		this.comment_time = comment_time;
	}

	public Comment(int comment_id, int post_id, String user_id, String comment_content, Date comment_time) {
		super();
		this.comment_id = comment_id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.comment_content = comment_content;
		this.comment_time = comment_time;
	}
}
