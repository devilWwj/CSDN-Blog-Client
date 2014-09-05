package com.xiaowu.blogclient.model;

/**
 * 评论实体类
 * 
 * @author wwj_748
 * 
 */
public class Comment {
	private String articleId; // 文章id
	private String blogId; // 博客id
	private String commentId; // 评论id
	private String content; // 评论内容
	private String parentId; // 父节点id
	private String postTime; // 发布时间
	private String replies; //
	private String username;// 用户名
	private String userface;// 用户头像
	private int type; // 评论类型，分topic和reply

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserface() {
		return userface;
	}

	public void setUserface(String userface) {
		this.userface = userface;
	}

	public int getType() {
		return type;
	}

	public void setType(int parent) {
		this.type = parent;
	}
	
	

}
