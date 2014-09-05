package com.xiaowu.blogclient;

import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaowu.blogclient.adapter.CommentAdapter;
import com.xiaowu.blogclient.model.Comment;
import com.xiaowu.blogclient.model.Page;
import com.xiaowu.blogclient.util.Constants;
import com.xiaowu.blogclient.util.DateUtil;
import com.xiaowu.blogclient.util.HttpUtil;
import com.xiaowu.blogclient.util.JsoupUtil;
import com.xiaowu.blogclient.util.URLUtil;

/**
 * 2014/8/13
 * 
 * 博客评论列表
 * 
 * @author wwj_748
 * 
 */
public class BlogCommentActivity extends Activity implements
		IXListViewRefreshListener, IXListViewLoadMore {

	private XListView listView;
	private CommentAdapter adapter;

	private ProgressBar progressBar;
	private ImageView reLoadImageView;

	private ImageView backBtn;
	private TextView commentTV;

	public static String commentCount = "";
	private Page page;
	private String filename;
	private int pageIndex = 1;
	private int pageSize = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		init();
		initComponent();

		listView.setRefreshTime(DateUtil.getDate()); // 设置刷新时间
		listView.startRefresh(); // 开始刷新
	}

	private void init() {
		filename = getIntent().getExtras().getString("filename"); // 获得文件名
		page = new Page();
		adapter = new CommentAdapter(this);
	}

	private void initComponent() {
		progressBar = (ProgressBar) findViewById(R.id.newsContentPro);
		reLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
		reLoadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("click");
				reLoadImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				new MainTask().execute(Constants.DEF_TASK_TYPE.REFRESH);
			}
		});

		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commentTV = (TextView) findViewById(R.id.comment);

		listView = (XListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(this);
		listView.setPullLoadEnable(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_no, R.anim.push_right_out);
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// 获得返回json字符串
			String temp = HttpUtil.httpGet(URLUtil.getCommentListURL(filename,
					page.getCurrentPage()));
			if (temp == null) {
				return Constants.DEF_RESULT_CODE.ERROR;
			}
			// 获得评论列表
			List<Comment> list = JsoupUtil.getBlogCommentList(temp,
					Integer.valueOf(page.getCurrentPage()), pageSize);
			if (list.size() == 0) {
				return Constants.DEF_RESULT_CODE.NO_DATA;
			}

			if (params[0].equals(Constants.DEF_TASK_TYPE.LOAD)) {
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			} else {
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == Constants.DEF_RESULT_CODE.ERROR) {
				Toast.makeText(getApplicationContext(), "网络信号不佳",
						Toast.LENGTH_SHORT).show();
				listView.stopRefresh(DateUtil.getDate());
				listView.stopLoadMore();
				reLoadImageView.setVisibility(View.VISIBLE);
			} else if (result == Constants.DEF_RESULT_CODE.NO_DATA) {
				Toast.makeText(getApplicationContext(), "无更多评论",
						Toast.LENGTH_SHORT).show();
				listView.stopLoadMore();
				listView.stopRefresh(DateUtil.getDate());
				commentTV.setText("共有评论：" + commentCount);
			} else if (result == Constants.DEF_RESULT_CODE.LOAD) {
				page.addPage();
				pageIndex++;
				adapter.notifyDataSetChanged();
				listView.stopLoadMore();
			} else if (result == Constants.DEF_RESULT_CODE.REFRESH) {
				adapter.notifyDataSetChanged();
				listView.stopRefresh(DateUtil.getDate());
				page.setPage(2);
				commentTV.setText("共有评论：" + commentCount);
			}
			progressBar.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onLoadMore() {
		new MainTask().execute(Constants.DEF_TASK_TYPE.LOAD);
	}

	@Override
	public void onRefresh() {
		page.setPage(1);
		new MainTask().execute(Constants.DEF_TASK_TYPE.REFRESH);
	}
}
