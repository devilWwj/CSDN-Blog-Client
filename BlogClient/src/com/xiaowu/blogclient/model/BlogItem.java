package com.xiaowu.blogclient.model;

/**
 * 博客实体类
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class BlogItem {
	private String title; // 标题
	private String link; // 文章链接
	private int id; // id
	private String date; // 博客发布时间
	private String imgLink; // 图片链接
	private String content; // 文章内容
	private String msg; // 消息
	private int type; // 博客分类

	private String viewTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getViewTime() {
		return viewTime;
	}

	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}

}
