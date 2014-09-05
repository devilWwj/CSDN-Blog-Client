package com.xiaowu.blogclient.util;

/**
 * 常量类
 * 
 * @author wwj_748
 * 
 */
public class Constants {
	// 博客每一项的类型
	public class DEF_BLOG_ITEM_TYPE {
		public static final int TITLE = 1; // 标题
		public static final int SUMMARY = 2; // 摘要
		public static final int CONTENT = 3; // 内容
		public static final int IMG = 4; // 图片
		public static final int BOLD_TITLE = 5; // 加粗标题
		public static final int CODE = 6; // 代码
	}

	public class DEF_NEWS_TYPE {
		public static final int YEJIE = 1;
		public static final int YIDONG = 2;
		public static final int YANFA = 3;
		public static final int ZAZHI = 4;
		public static final int YUNJISUAN = 5;
	}

	// 评论类型
	public class DEF_COMMENT_TYPE {
		public static final int PARENT = 1;
		public static final int CHILD = 2;
	}

	// 操作结果类型
	public class DEF_RESULT_CODE {
		public static final int ERROR = 1; // 错误
		public static final int NO_DATA = 2;// 无数据
		public static final int REFRESH = 3;// 刷新
		public static final int LOAD = 4; // 加载
		public static final int FIRST = 5;// 第一次加载
	}

	// 任务类型
	public class DEF_TASK_TYPE {
		public static final String FIRST = "first";
		public static final String NOR_FIRST = "not_first";
		public static final String REFRESH = "REFRESH";
		public static final String LOAD = "LOAD";
	}

	/**
	 * 文章类型
	 */
	public class DEF_ARTICLE_TYPE {
		public static final int HOME = 0;		// 首页
		public static final int ANDROID = 1;	// Android
		public static final int COCOS2DX = 2;	// Cocos2dx
		public static final int INTERVIEW = 3;	// 面试宝典
		public static final int LUA = 4;		// Lua
		public static final int DESIGN_PATTERN = 5; // 设计模式
		public static final int XIAOWU_RECORD = 6;	// 记录点滴
		public static final int NETWORK_PROT = 7;	// 网络协议
		public static final int GO = 8;			// go语言
		public static final int JIAN_ZHAN = 9;	// 建站经验
	}
}
