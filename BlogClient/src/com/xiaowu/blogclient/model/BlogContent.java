package com.xiaowu.blogclient.model;

public class BlogContent {
	private String content;
	private boolean multiPage;
	private String nextPageLink;
	private boolean firstPage = true;
	private boolean endPage;
	public boolean isMultiPage() {
		return multiPage;
	}
	public void setMultiPage(boolean multiPage) {
		this.multiPage = multiPage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNextPageLink() {
		return nextPageLink;
	}
	public void setNextPageLink(String nextPageLink) {
		this.nextPageLink = nextPageLink;
	}
	public boolean isFirstPage() {
		return firstPage;
	}
	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}
	public boolean isEndPage() {
		return endPage;
	}
	public void setEndPage(boolean endPage) {
		this.endPage = endPage;
	}
}
