package com.xiaowu.blogclient;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.xiaowu.blogclient.adapter.BlogListAdapter;
import com.xiaowu.blogclient.model.BlogItem;
import com.xiaowu.blogclient.model.Page;
import com.xiaowu.blogclient.util.Constants;
import com.xiaowu.blogclient.util.DB;
import com.xiaowu.blogclient.util.HttpUtil;
import com.xiaowu.blogclient.util.JsoupUtil;
import com.xiaowu.blogclient.util.URLUtil;

/**
 * Fragment页面
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class BlogFrag extends Fragment implements IXListViewRefreshListener,
		IXListViewLoadMore {
	private XListView blogListView;// 博客列表
	private View noBlogView; // 无数据时显示
	private BlogListAdapter adapter;// 列表适配器

	private boolean isLoad = false; // 是否加载
	private int blogType = 0; // 博客类别
	private Page page; // 页面引用

	private DB db; // 数据库引用
	private String refreshDate = ""; // 刷新日期

	public BlogFrag(int blogType) {
		this.blogType = blogType;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initComponent();
		if (isLoad == false) {
			isLoad = true;
			refreshDate = getDate();
			blogListView.setRefreshTime(refreshDate);
			// 加载数据库中的数据
			List<BlogItem> list = db.query(blogType);
			adapter.setList(list);
			adapter.notifyDataSetChanged();

			blogListView.startRefresh(); // 开始刷新

		} else {
			blogListView.NotRefreshAtBegin(); // 不开始刷新
		}
		Log.e("NewsFrag", "onActivityCreate");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("NewsFrag", "onCreateView");
		return inflater.inflate(R.layout.activity_main, null);
	}

	// 初始化
	private void init() {
		db = new DB(getActivity());
		adapter = new BlogListAdapter(getActivity());
		page = new Page();
		page.setPageStart();
	}

	// 初始化组件
	private void initComponent() {
		blogListView = (XListView) getView().findViewById(R.id.blogListView);
		blogListView.setAdapter(adapter);// 设置适配器
		blogListView.setPullRefreshEnable(this);// 设置可下拉刷新
		blogListView.setPullLoadEnable(this);// 设置可上拉加载
		// 设置列表项点击事件
		blogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获得博客列表项
				BlogItem item = (BlogItem) adapter.getItem(position - 1);
				Intent i = new Intent();
				i.setClass(getActivity(), BlogDetailActivity.class);
				i.putExtra("blogLink", item.getLink());
				startActivity(i);
				// 动画过渡
				getActivity().overridePendingTransition(R.anim.push_left_in,
						R.anim.push_no);
				Log.e("position", "" + position);
			}
		});

		noBlogView = getView().findViewById(R.id.noBlogLayout);
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// 获取网页html数据
			String temp = HttpUtil.httpGet(params[0]);
			if (temp == null) {
				return Constants.DEF_RESULT_CODE.ERROR;
			}
			// 解析html页面获取列表
			List<BlogItem> list = JsoupUtil.getBlogItemList(blogType, temp);
			if (list.size() == 0) {
				return Constants.DEF_RESULT_CODE.NO_DATA;
			}
			// 刷新动作
			if (params[1].equals("refresh")) {
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			} else {// 加载更多
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// 通知列表数据更新
			adapter.notifyDataSetChanged();
			switch (result) {
			case Constants.DEF_RESULT_CODE.ERROR: // 错误
				Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG);
				blogListView.stopRefresh(getDate());
				blogListView.stopLoadMore();
				break;
			case Constants.DEF_RESULT_CODE.NO_DATA: // 无数据
				// Toast.makeText(getActivity(), "无更多加载内容", Toast.LENGTH_LONG)
				// .show();
				blogListView.stopLoadMore();
				// noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				break;
			case Constants.DEF_RESULT_CODE.REFRESH: // 刷新
				blogListView.stopRefresh(getDate());

				db.delete(blogType);
				db.insert(adapter.getList());// 保存到数据库
				if (adapter.getCount() == 0) {
					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				}
				break;
			case Constants.DEF_RESULT_CODE.LOAD:
				blogListView.stopLoadMore();
				page.addPage();
				if (adapter.getCount() == 0) {
					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				}
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

	}

	// 加载更多时调用
	@Override
	public void onLoadMore() {
		System.out.println("loadmore");
		new MainTask()
				.execute(
						URLUtil.getBlogListURL(blogType, page.getCurrentPage()),
						"load");
	}

	@Override
	public void onRefresh() {
		System.out.println("refresh");
		page.setPageStart();
		new MainTask().execute(URLUtil.getRefreshBlogListURL(blogType),
				"refresh");
	}

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
				Locale.CHINA);
		return sdf.format(new java.util.Date());
	}

}
