package com.xiaowu.blogclient;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.XListView;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xiaowu.blogclient.adapter.BlogDetailAdapter;
import com.xiaowu.blogclient.util.Constants;
import com.xiaowu.blogclient.util.JsoupUtil;
import com.xiaowu.blogclient.util.HttpUtil;

/**
 * 博客详细内容界面
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class BlogDetailActivity extends Activity implements IXListViewLoadMore {
	private XListView listView; // 列表控件
	private BlogDetailAdapter blogDetailAdapter; // 内容适配器

	private ProgressBar progressBar; // 进度条
	private ImageView reLoadImageView; // 重新加载的图片

	private ImageView backBtn; // 回退按钮
	private ImageView commentBtn; // 评论按钮

	public static String url;
	private String filename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_detail);

		init();
		initComponent();

		// 执行异步加载
		new MainTask().execute(url, Constants.DEF_TASK_TYPE.FIRST);
	}

	// 初始化
	private void init() {
		blogDetailAdapter = new BlogDetailAdapter(this);
		url = getIntent().getExtras().getString("blogLink");
		filename = url.substring(url.lastIndexOf("/") + 1);
		System.out.println("filename--->" + filename);
	}

	// 初始化组件
	private void initComponent() {
		progressBar = (ProgressBar) findViewById(R.id.blogContentPro);
		reLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
		reLoadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				reLoadImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);

			}
		});

		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		commentBtn = (ImageView) findViewById(R.id.comment);
		commentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent();
				i.setClass(BlogDetailActivity.this, BlogCommentActivity.class);
				i.putExtra("filename", filename);
				startActivity(i);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
			}
		});

		listView = (XListView) findViewById(R.id.listview);
		listView.setAdapter(blogDetailAdapter);
		listView.setPullLoadEnable(this);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取点击列表项的状态
				int state = blogDetailAdapter.getList().get(position - 1)
						.getState();
				switch (state) {
				case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 点击的是图片
					String url = blogDetailAdapter.getList().get(position - 1)
							.getImgLink();
					Intent i = new Intent();
					i.setClass(BlogDetailActivity.this, ImageActivity.class);
					i.putExtra("url", url);
					startActivity(i);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String temp = HttpUtil.httpGet(params[0]);
			if (temp == null) {
				if (params[1].equals(Constants.DEF_TASK_TYPE.FIRST)) {
					return Constants.DEF_RESULT_CODE.FIRST;
				} else {
					return Constants.DEF_RESULT_CODE.ERROR;
				}
			}
			blogDetailAdapter.addList(JsoupUtil.getContent(url, temp));
			if (params[1].equals(Constants.DEF_TASK_TYPE.FIRST)) {
				return Constants.DEF_RESULT_CODE.REFRESH;
			}
			return Constants.DEF_RESULT_CODE.LOAD;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == Constants.DEF_RESULT_CODE.FIRST) {
				Toast.makeText(getApplicationContext(), "网络信号不佳",
						Toast.LENGTH_LONG).show();
				reLoadImageView.setVisibility(View.VISIBLE);
			} else if (result == Constants.DEF_RESULT_CODE.ERROR) {
				listView.stopLoadMore();
			} else if (result == Constants.DEF_RESULT_CODE.REFRESH) {
				blogDetailAdapter.notifyDataSetChanged();
			} else {
				blogDetailAdapter.notifyDataSetChanged();
				listView.stopLoadMore();
			}
			progressBar.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}

	}

	@Override
	public void onLoadMore() {
		if (!JsoupUtil.contentLastPage) {
			new MainTask().execute(url, Constants.DEF_TASK_TYPE.NOR_FIRST);
		} else {
			listView.stopLoadMore(" -- THE END --");
		}
	}

}
