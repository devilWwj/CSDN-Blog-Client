package com.xiaowu.blogclient.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaowu.blogclient.BlogCommentActivity;
import com.xiaowu.blogclient.model.Blog;
import com.xiaowu.blogclient.model.BlogItem;
import com.xiaowu.blogclient.model.Blogger;
import com.xiaowu.blogclient.model.Comment;

/**
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class JsoupUtil {
	public static boolean contentFirstPage = true; // 第一页
	public static boolean contentLastPage = true; // 最后一页
	public static boolean multiPages = false; // 多页
	private static final String BLOG_URL = "http://blog.csdn.net"; // CSDN博客地址

	// 链接样式文件，代码块高亮的处理
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJScript.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJava.js\"></script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>";

	public static void resetPages() {
		contentFirstPage = true;
		contentLastPage = true;
		multiPages = false;
	}

	/**
	 * 使用Jsoup解析html文档
	 * 
	 * @param blogType
	 * @param str
	 * @return
	 */
	public static List<BlogItem> getBlogItemList(int blogType, String str) {
		// Log.e("URL---->", str);
		List<BlogItem> list = new ArrayList<BlogItem>();
		// 获取文档对象
		Document doc = Jsoup.parse(str);
		// Log.e("doc--->", doc.toString());
		// 获取class="article_item"的所有元素
		Elements blogList = doc.getElementsByClass("article_item");
		// Log.e("elements--->", blogList.toString());

		for (Element blogItem : blogList) {
			BlogItem item = new BlogItem();
			String title = blogItem.select("h1").text(); // 得到标题
			// System.out.println("title----->" + title);
			String description = blogItem.select("div.article_description")
					.text();
			// System.out.println("descrition--->" + description);
			String msg = blogItem.select("div.article_manage").text();
			// System.out.println("msg--->" + msg);
			String date = blogItem.getElementsByClass("article_manage").get(0)
					.text();
			// System.out.println("date--->" + date);
			String link = BLOG_URL
					+ blogItem.select("h1").select("a").attr("href");
			// System.out.println("link--->" + link);
			item.setTitle(title);
			item.setMsg(msg);
			item.setContent(description);
			item.setDate(date);
			item.setLink(link);
			item.setType(blogType);

			// 没有图片
			item.setImgLink(null);
			list.add(item);

		}
		return list;
	}

	/**
	 * 扒取传入url地址的博客详细内容
	 * 
	 * @param url
	 * @param str
	 * @return
	 */
	public static List<Blog> getContent(String url, String str) {
		List<Blog> list = new ArrayList<Blog>();

		// 获取文档内容
		Document doc = Jsoup.parse(str);

		// 获取class="details"的元素
		Element detail = doc.getElementsByClass("details").get(0);
		detail.select("script").remove(); // 删除每个匹配元素的DOM。

		// 获取标题
		Element title = detail.getElementsByClass("article_title").get(0);
		Blog blogTitle = new Blog();
		blogTitle.setState(Constants.DEF_BLOG_ITEM_TYPE.TITLE); // 设置状态
		blogTitle.setContent(ToDBC(title.text())); // 设置标题内容

		// 获取文章内容
		Element content = detail.select("div.article_content").get(0);

		// 获取所有标签为<a的元素
		Elements as = detail.getElementsByTag("a");
		for (int b = 0; b < as.size(); b++) {
			Element blockquote = as.get(b);
			// 改变这个元素的标记。例如,<span>转换为<div> 如el.tagName("div");。
			blockquote.tagName("bold"); // 转为粗体
		}

		Elements ss = detail.getElementsByTag("strong");
		for (int b = 0; b < ss.size(); b++) {
			Element blockquote = ss.get(b);
			blockquote.tagName("bold");
		}

		// 获取所有标签为<p的元素
		Elements ps = detail.getElementsByTag("p");
		for (int b = 0; b < ps.size(); b++) {
			Element blockquote = ps.get(b);
			blockquote.tagName("body");
		}

		// 获取所有引用元素
		Elements blockquotes = detail.getElementsByTag("blockquote");
		for (int b = 0; b < blockquotes.size(); b++) {
			Element blockquote = blockquotes.get(b);
			blockquote.tagName("body");
		}

		// 获取所有标签为<ul的元素
		Elements uls = detail.getElementsByTag("ul");
		for (int b = 0; b < uls.size(); b++) {
			Element blockquote = uls.get(b);
			blockquote.tagName("body");
		}

		// 找出粗体
		Elements bs = detail.getElementsByTag("b");
		for (int b = 0; b < bs.size(); b++) {
			Element bold = bs.get(b);
			bold.tagName("bold");
		}

		// 遍历博客内容中的所有元素
		for (int j = 0; j < content.children().size(); j++) {
			Element c = content.child(j); // 获取每个元素

			// 抽取出图片
			if (c.select("img").size() > 0) {
				Elements imgs = c.getElementsByTag("img");
				System.out.println("img");
				for (Element img : imgs) {
					if (!img.attr("src").equals("")) {
						Blog blogImgs = new Blog();
						// 大图链接
						if (!img.parent().attr("href").equals("")) {
							blogImgs.setImgLink(img.parent().attr("href"));
							System.out.println("href="
									+ img.parent().attr("href"));
							if (img.parent().parent().tagName().equals("p")) {
								// img.parent().parent().remove();
							}
							img.parent().remove();
						}
						blogImgs.setContent(img.attr("src"));
						blogImgs.setImgLink(img.attr("src"));
						System.out.println(blogImgs.getContent());
						blogImgs.setState(Constants.DEF_BLOG_ITEM_TYPE.IMG);
						list.add(blogImgs);
					}
				}
			}
			c.select("img").remove();

			// 获取博客内容
			Blog blogContent = new Blog();
			blogContent.setState(Constants.DEF_BLOG_ITEM_TYPE.CONTENT);

			if (c.text().equals("")) {
				continue;
			} else if (c.children().size() == 1) {
				if (c.child(0).tagName().equals("bold")
						|| c.child(0).tagName().equals("span")) {
					if (c.ownText().equals("")) {
						// 小标题，咖啡色
						blogContent
								.setState(Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE);
					}
				}
			}

			// 代码
			if (c.select("pre").attr("name").equals("code")) {
				blogContent.setState(Constants.DEF_BLOG_ITEM_TYPE.CODE);
				blogContent.setContent(ToDBC(c.outerHtml()) + linkCss);
			} else {
				blogContent.setContent(ToDBC(c.outerHtml()));
			}
			list.add(blogContent);
		}

		return list;
	}

	/**
	 * 获取博客评论列表
	 * 
	 * @unused 无用
	 * @param str
	 * @return
	 */
	// public static List<Comment> getComment(String str) {
	// List<Comment> list = new ArrayList<Comment>();
	//
	// // 获取文档对象
	// Document doc = Jsoup.parse(str);
	// // 获得id为comment_list的元素
	// Element commentClass = doc.getElementsByClass("comment_class").get(0);
	//
	// Element commentList = commentClass.getElementById("comment_list");
	//
	// // 获得所有评论主题
	// Elements commentTopics = commentList
	// .select("div.comment_item.comment_topic");
	// // 遍历所有评论主题
	// for (Element commentTopic : commentTopics) {
	// Comment topic = new Comment();
	// topic.setName(commentTopic.select("a.username").text());
	// topic.setDate(commentTopic.select("span.ptime").text());
	// topic.setContent(commentTopic.select("dd.comment_body").text());
	// topic.setPic(commentTopic.select("dd.comment_userface")
	// .select("img").attr("src"));
	//
	// // 获取评论回复
	// Elements commentReplies = commentTopic
	// .getElementsByClass("comment_reply");
	// if (commentReplies.size() == 0) {
	// topic.setReplyCount("");
	// } else {
	// topic.setReplyCount("回复: " + commentReplies.size());
	// }
	// topic.setType(Constants.DEF_COMMENT_TYPE.PARENT);
	// list.add(topic);
	//
	// if (commentReplies.size() != 0) {
	// for (Element replyElement : commentReplies) {
	// Comment reply = new Comment();
	// reply.setName(replyElement.select("a.username").text());
	// reply.setDate(replyElement.select("span.ptime").text());
	// reply.setContent(replyElement.select("dd.comment_body")
	// .text());
	// reply.setPic(replyElement.select("dd.comment_userface")
	// .select("img").attr("src"));
	// Elements commentReplies2 = replyElement
	// .getElementsByClass("comment_reply");
	// reply.setReplyCount("" + commentReplies2.size());
	// reply.setType(Constants.DEF_COMMENT_TYPE.CHILD);
	//
	// list.add(reply);
	// }
	// }
	// }
	// return list;
	//
	// }

	/**
	 * 获取博文评论列表
	 * 
	 * @param str
	 *            json字符串
	 * @return
	 */
	public static List<Comment> getBlogCommentList(String str, int pageIndex,
			int pageSize) {
		List<Comment> list = new ArrayList<Comment>();
		try {
			// 创建一个json对象
			JSONObject jsonObject = new JSONObject(str);
			JSONArray jsonArray = jsonObject.getJSONArray("list"); // 获取json数组
			int index = 0;
			int len = jsonArray.length();
			BlogCommentActivity.commentCount = String.valueOf(len); // 评论条数
			// 如果评论数大于20
			if (len > 20) {
				index = (pageIndex * pageSize) - 20;
			}

			if (len < pageSize && pageIndex > 1) {
				return list;
			}

			if ((pageIndex * pageSize) < len) {
				len = pageIndex * pageSize;
			}

			for (int i = index; i < len; i++) {
				JSONObject item = jsonArray.getJSONObject(i);
				String commentId = item.getString("CommentId");
				String content = item.getString("Content");
				String username = item.getString("UserName");
				String parentId = item.getString("ParentId");
				String postTime = item.getString("PostTime");
				String userface = item.getString("Userface");

				Comment comment = new Comment();
				comment.setCommentId(commentId);
				comment.setContent(content);
				comment.setUsername(username);
				comment.setParentId(parentId);
				comment.setPostTime(postTime);
				comment.setUserface(userface);

				if (parentId.equals("0")) {
					// 如果parentId为0的话，表示它是评论的topic
					comment.setType(Constants.DEF_COMMENT_TYPE.PARENT);
				} else {
					comment.setType(Constants.DEF_COMMENT_TYPE.CHILD);
				}
				list.add(comment);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获得博主个人资料
	 * 
	 * @param str
	 * @return
	 */
	public static Blogger getBloggerInfo(String str) {

		// 获取文档内容
		Document doc = Jsoup.parse(str);

		Element profile = doc.getElementsByClass("panel").get(0);

		Element profileBody = profile.select("ul.panel_body.profile").get(0);

		Element userface = profileBody.getElementById("blog_userface");
		String userfaceLink = userface.select("a").select("img").attr("src"); // 得到头像链接
		String username = userface.getElementsByTag("a").get(1).text(); // 用户名

		Element blog_rank = profileBody.getElementById("blog_rank");
		Element blog_statistics = profileBody.getElementById("blog_statistics");

		Elements rankLi = blog_rank.select("li");
		StringBuilder sb = new StringBuilder();
		String rankStr = "";
		for (Element rank : rankLi) {
			sb.append(rank.text()).append("|");
		}
		rankStr = sb.toString();

		String statistics = "";
		StringBuilder sb2 = new StringBuilder();
		Elements blogLi = blog_statistics.select("li");
		for (Element info : blogLi) {
			sb2.append(info.text()).append("|");
		}
		statistics = sb2.toString();

		Blogger blogger = new Blogger();
		blogger.setUserface(userfaceLink);
		blogger.setUsername(username);
		blogger.setRank(rankStr);
		blogger.setStatistics(statistics);

		return blogger;
	}

	/**
	 * 半角转换为全角 全角---指一个字符占用两个标准字符位置。 半角---指一字符占用一个标准的字符位置。
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
