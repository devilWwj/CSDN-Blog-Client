package com.xiaowu.blogclient.model;

/**
 * 博主个人信息
 * 
 * @author wwj_748
 * @date 2014/9/4
 */
public class Blogger {
	private String userface; // 博主头像
	private String username; // 博主名称
	private String[] medals; // 获得勋章
	private String rank; // 排名相关
	private String statistics; // 博文数据

	public String getUserface() {
		return userface;
	}

	public void setUserface(String userface) {
		this.userface = userface;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getMedals() {
		return medals;
	}

	public void setMedals(String[] medals) {
		this.medals = medals;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStatistics() {
		return statistics;
	}

	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}

}
