package com.xiaowu.blogclient.net;

import java.util.ArrayList;
import java.util.List;

import com.xiaowu.blogclient.util.APIUtil;

/**
 * 接口封装
 * 
 * @author wwj_748
 * @date 2014/8/24
 */
public class Potocol {
	// 你本人的博客用户名和密码
	public static final String USER_NAME = "822583955@qq.com";
	public static final String PASSWROD = "";

	/**
	 * 拼接Oauth2.0授权请求字符串 http://api.csdn.net/oauth2/access_token?
	 * client_id=YOUR_API_KEY
	 * &client_secret=YOUR_API_SECRET&grant_type=password&username
	 * =USER_NAME&password=PASSWORD
	 * 
	 * @param username
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return
	 */
	public static String getOauthString(String username, String pwd) {
		StringBuffer sb = new StringBuffer();
		sb.append(APIUtil.OAUTH_URL).append("?").append("client_id=")
				.append(APIUtil.APP_KEY).append("&client_secret=")
				.append(APIUtil.APP_SCRECT).append("&grant_type=").append(pwd)
				.append("&username=").append(username).append("&password=")
				.append(pwd);
		return sb.toString();
	}

	
	public static List<Parameter> getOauthParams(String username, String pwd) {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("client_id", APIUtil.APP_KEY));
		params.add(new Parameter("client_secret", APIUtil.APP_SCRECT));
		params.add(new Parameter("grant_type", pwd));
		params.add(new Parameter("username", username));
		params.add(new Parameter("password", pwd));
		return params;
	}

	/**
	 * 获取文章评论的请求字符串
	 * 
	 * @param accessToken
	 *            授权过后得到
	 * @param article
	 *            文章id
	 * @param page
	 *            当前页码，默认1
	 * @param size
	 *            每页条数，默认15
	 * @return
	 */
	public static String getArticleComment(String accessToken, int article,
			int page, int size) {
		StringBuffer sb = new StringBuffer();
		sb.append(APIUtil.GET_ARTICAL_COMMENT).append("?")
				.append("access_token=").append(accessToken)
				.append("&article=").append(article).append("&page=")
				.append(page).append("&size=").append(size);
		return sb.toString();
	}
}
