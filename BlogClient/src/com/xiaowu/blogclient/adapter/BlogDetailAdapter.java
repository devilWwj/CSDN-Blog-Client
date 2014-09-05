package com.xiaowu.blogclient.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xiaowu.blogclient.R;
import com.xiaowu.blogclient.model.Blog;
import com.xiaowu.blogclient.util.Constants;
import com.xiaowu.blogclient.util.MyTagHandler;

/**
 * 博客内容适配器
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class BlogDetailAdapter extends BaseAdapter {
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<Blog> list;

	private SpannableStringBuilder htmlSpannable;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public BlogDetailAdapter(Context context) {
		super();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		list = new ArrayList<Blog>();

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

	}

	public void setList(List<Blog> list) {
		this.list = list;
	}

	public void addList(List<Blog> list) {
		this.list.addAll(list);
	}

	public void clearList() {
		this.list.clear();
	}

	public List<Blog> getList() {
		return list;
	}

	public void removeItem(int position) {
		if (list.size() > 0) {
			list.remove(position);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Blog item = list.get(position);
		if (null == convertView) {
			holder = new ViewHolder();
			switch (item.getState()) {
			case Constants.DEF_BLOG_ITEM_TYPE.TITLE:// 显示标题
				convertView = layoutInflater.inflate(
						R.layout.article_detail_title_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.SUMMARY: // 摘要
				convertView = layoutInflater.inflate(
						R.layout.article_detail_summary_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.CONTENT: // 内容
				convertView = layoutInflater.inflate(
						R.layout.article_detail_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 图片
				convertView = layoutInflater.inflate(
						R.layout.article_detail_img_item, null);
				holder.image = (ImageView) convertView
						.findViewById(R.id.imageView);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE: // 加粗标题
				convertView = layoutInflater.inflate(
						R.layout.article_detail_bold_title_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.CODE: // 代码
				convertView = layoutInflater.inflate(
						R.layout.article_detail_code_item, null);
				holder.code = (WebView) convertView
						.findViewById(R.id.code_view);
//				holder.code.getSettings().setUseWideViewPort(true);
//				holder.code.getSettings().setJavaScriptEnabled(true);
//				holder.code.getSettings().setSupportZoom(true);
//				holder.code.getSettings().setBuiltInZoomControls(false);
//				holder.code.getSettings().setLoadWithOverviewMode(true);
				break;
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		System.out.println(item.getContent());

		if (null != item) {
			switch (item.getState()) {
			case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 图片，异步加载
				imageLoader.displayImage(item.getContent(), holder.image,
						options);
				break;
			case Constants.DEF_BLOG_ITEM_TYPE.CODE: // 代码，格式显示
				holder.code.loadDataWithBaseURL(null, item.getContent(),
						"text/html", "utf-8", null);
				break;
			default:
				holder.content.setText(Html.fromHtml(item.getContent(), null,
						new MyTagHandler()));
				break;
			}
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 6;
	}

	@Override
	public int getItemViewType(int position) {
		switch (list.get(position).getState()) {
		case Constants.DEF_BLOG_ITEM_TYPE.TITLE:
			return 0;
		case Constants.DEF_BLOG_ITEM_TYPE.SUMMARY:
			return 1;
		case Constants.DEF_BLOG_ITEM_TYPE.CONTENT:
			return 2;
		case Constants.DEF_BLOG_ITEM_TYPE.IMG:
			return 3;
		case Constants.DEF_BLOG_ITEM_TYPE.BOLD_TITLE:
			return 4;
		case Constants.DEF_BLOG_ITEM_TYPE.CODE:
			return 5;
		}
		return 1;
	}

	@Override
	public boolean isEnabled(int position) {
		switch (list.get(position).getState()) {
		case Constants.DEF_BLOG_ITEM_TYPE.IMG:
			return true;
		default:
			return false;
		}
	}

	private class ViewHolder {
		TextView id;
		TextView date;
		TextView title;
		TextView content;
		ImageView image;
		WebView code;
	}
}
