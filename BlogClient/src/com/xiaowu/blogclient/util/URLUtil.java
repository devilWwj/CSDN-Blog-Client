package com.xiaowu.blogclient.util;

/**
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class URLUtil {
	// 一系列博客URL
	public static String BASE_URL = "http://blog.csdn.net/wwj_748/article/list";
	public static String ARTICLE_URL_ANDROID_1 = "http://blog.csdn.net/wwj_748/article/category/1529925";
	public static String ARTICLE_URL_ANDROID_2 = "http://blog.csdn.net/wwj_748/article/category/1342194";
	public static String ARTICLE_URL_ANDROID_3 = "http://blog.csdn.net/wwj_748/article/category/1414168";
	public static String ARTICLE_URL_ANDROID_4 = "http://blog.csdn.net/wwj_748/article/category/1415838";
	public static String ARTICLE_URL_ANDROID_5 = "http://blog.csdn.net/wwj_748/article/category/1572115";
	public static String ARTICLE_URL_ANDROID_6 = "http://blog.csdn.net/wwj_748/article/category/1119319";
	public static String ARTICLE_URL_ANDROID_7 = "http://blog.csdn.net/wwj_748/article/category/1833855";
	public static String ARTICLE_URL_ANDROID_8 = "http://blog.csdn.net/wwj_748/article/category/2063969";
	public static String ARTICLE_URL_ANDROID_9 = "http://blog.csdn.net/wwj_748/article/category/1773747";
	public static String ARTICLE_URL_ANDROID_10 = "http://blog.csdn.net/wwj_748/article/category/1845765";
	public static String ARTICLE_URL_ANDROID_11 = "http://blog.csdn.net/wwj_748/article/category/1846803";
	public static String ARTICLE_URL_ANDROID_12 = "http://blog.csdn.net/wwj_748/article/category/1863541";
	public static String ARTICLE_URL_COCOS2DX = "http://blog.csdn.net/wwj_748/article/category/2143091";
	public static String ARTICLE_URL_XIAOWU_INTERVIEW = "http://blog.csdn.net/wwj_748/article/category/1399759";
	public static String ARTICLE_URL_LUA = "http://blog.csdn.net/wwj_748/article/category/2293377";
	public static String ARTICLE_URL_DESIGN_PATTERN = "http://blog.csdn.net/wwj_748/article/category/1401254";
	public static String ARTICLE_URL_XIAOWU_RECORD = "http://blog.csdn.net/wwj_748/article/category/1081018";
	public static String ARTICLE_URL_NETWORK_POTOCOL = "http://blog.csdn.net/wwj_748/article/category/1935343";
	public static String ARTICLE_URL_GO = "http://blog.csdn.net/wwj_748/article/category/1908067";
	public static String ARTICLE_URL_IPHONE = "http://blog.csdn.net/wwj_748/article/category/2173325";
	public static String ARTICLE_URL_PHP = "http://blog.csdn.net/wwj_748/article/category/2198515";
	public static String ARTICLE_URL_U3D = "http://blog.csdn.net/wwj_748/article/category/2213443";
	public static String ARTICLE_URL_JIAN_ZHAN = "http://blog.csdn.net/wwj_748/article/category/2225849";

	/**
	 * 获取博客列表的URL
	 * 
	 * @param blogType
	 *            博客类型
	 * @param page
	 *            页数
	 * @return
	 */
	public static String getBlogListURL(int blogType, String page) {
		String url = "";
		switch (blogType) {
		case Constants.DEF_ARTICLE_TYPE.HOME:
			url = BASE_URL;
			break;
		case Constants.DEF_ARTICLE_TYPE.ANDROID:
			url = ARTICLE_URL_ANDROID_6;
			break;
		case Constants.DEF_ARTICLE_TYPE.COCOS2DX:
			url = ARTICLE_URL_COCOS2DX;
			break;
		case Constants.DEF_ARTICLE_TYPE.INTERVIEW:
			url = ARTICLE_URL_XIAOWU_INTERVIEW;
			break;
		case Constants.DEF_ARTICLE_TYPE.LUA:
			url = ARTICLE_URL_LUA;
			break;
		case Constants.DEF_ARTICLE_TYPE.DESIGN_PATTERN:
			url = ARTICLE_URL_DESIGN_PATTERN;
			break;
		case Constants.DEF_ARTICLE_TYPE.XIAOWU_RECORD:
			url = ARTICLE_URL_XIAOWU_RECORD;
			break;
		case Constants.DEF_ARTICLE_TYPE.NETWORK_PROT:
			url = ARTICLE_URL_NETWORK_POTOCOL;
			break;
		case Constants.DEF_ARTICLE_TYPE.GO:
			url = ARTICLE_URL_GO;
			break;
		case Constants.DEF_ARTICLE_TYPE.JIAN_ZHAN:
			url = ARTICLE_URL_JIAN_ZHAN;
			break;
		default:
			break;
		}
		url = url + "/" + page;
		return url;
	}

	/**
	 * 获取刷新博客的URL
	 * 
	 * @param blogType
	 *            博客类型
	 * @return
	 */
	public static String getRefreshBlogListURL(int blogType) {
		String url = "";
		switch (blogType) {
		case Constants.DEF_ARTICLE_TYPE.HOME:
			url = BASE_URL;
			break;
		case Constants.DEF_ARTICLE_TYPE.ANDROID:
			url = ARTICLE_URL_ANDROID_6;
			break;
		case Constants.DEF_ARTICLE_TYPE.COCOS2DX:
			url = ARTICLE_URL_COCOS2DX;
			break;
		case Constants.DEF_ARTICLE_TYPE.INTERVIEW:
			url = ARTICLE_URL_XIAOWU_INTERVIEW;
			break;
		case Constants.DEF_ARTICLE_TYPE.LUA:
			url = ARTICLE_URL_LUA;
			break;
		case Constants.DEF_ARTICLE_TYPE.DESIGN_PATTERN:
			url = ARTICLE_URL_DESIGN_PATTERN;
			break;
		case Constants.DEF_ARTICLE_TYPE.XIAOWU_RECORD:
			url = ARTICLE_URL_XIAOWU_RECORD;
			break;
		case Constants.DEF_ARTICLE_TYPE.NETWORK_PROT:
			url = ARTICLE_URL_NETWORK_POTOCOL;
			break;
		case Constants.DEF_ARTICLE_TYPE.GO:
			url = ARTICLE_URL_GO;
			break;
		case Constants.DEF_ARTICLE_TYPE.JIAN_ZHAN:
			url = ARTICLE_URL_JIAN_ZHAN;
			break;
		}
		url = url + "/1";
		return url;
	}

	/**
	 * 返回博文评论列表链接
	 * 
	 * @param filename
	 *            文件名
	 * @param pageIndex
	 *            页数
	 * @return
	 */
	public static String getCommentListURL(String filename, String pageIndex) {
		return "http://blog.csdn.net/wwj_748/comment/list/" + filename
				+ "?page=" + pageIndex;
	}

}
