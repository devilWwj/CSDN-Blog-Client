package com.xiaowu.blogclient;

import static com.xiaowu.blogclient.umeng.SocializeConfigDemo.DESCRIPTOR;
import net.youmi.android.spot.SpotManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.viewpagerindicator.PageIndicator;
import com.xiaowu.blogclient.adapter.TabAdapter;
import com.xiaowu.blogclient.net.Potocol;
import com.xiaowu.blogclient.net.SyncHttp;
import com.xiaowu.blogclient.view.CircleImageView;

/**
 * 主界面
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class MainTabActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private CircleImageView headIcon;
	private ImageButton moreButton;

	private Context mContext = null;
	// sdk controller
	private UMSocialService mController = null;
	// 要分享的文字内容
	private String mShareContent = "";
	// 要分享的图片
	private UMImage mUMImgBitmap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);

		// 检测应用更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setDeltaUpdate(true);
		UmengUpdateAgent.update(this);

		FragmentPagerAdapter adapter = new TabAdapter(
				getSupportFragmentManager());

		// 视图切换器
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// 页面指示器
		PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		initSlidingMenu(savedInstanceState);

		headIcon = (CircleImageView) findViewById(R.id.head_icon);
		moreButton = (ImageButton) findViewById(R.id.personCenter);
		headIcon.setOnClickListener(this);
		moreButton.setOnClickListener(this);

		initConfig();

	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		// 设置右侧滑动菜单
		setBehindContentView(R.layout.menu_frame_right);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_two, new PersonCenterFragment())
				.commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 实例化滑动菜单对象
		sm = getSlidingMenu();
		// 设置可以左右滑动菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);
		sm.setBackgroundResource(R.drawable.biz_news_local_weather_bg_big);

	}

	/**
	 * @功能描述 : 初始化与SDK相关的成员变量
	 */
	private void initConfig() {
		mContext = this;
		mController = UMServiceFactory.getUMSocialService(DESCRIPTOR);

		// 要分享的文字内容
		mShareContent = "小巫CSDN博客客户端，CSDN移动开发专家——IT_xiao小巫的专属客户端，你值得拥有。";
		mController.setShareContent(mShareContent);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);

		mUMImgBitmap = new UMImage(mContext, bitmap);

		// 添加新浪和qq空间的SSO授权支持
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO支持
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

	}

	private void openShareBoard() {
		mController.openShare(this, false);
	}

	// 按返回键触发
	@Override
	public void onBackPressed() {
		// 弹出退出对话框
		Builder dialog = new AlertDialog.Builder(MainTabActivity.this)
				.setMessage("您确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// 退出程序
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
		dialog.show();

		// // 如果有需要，可以点击后退关闭插播广告。
		// if (!SpotManager.getInstance(this).disMiss(true)) {
		// super.onBackPressed();
		// }
	}

	/**
	 * 客户端授权认证
	 * 
	 * @author Administrator
	 * 
	 */
	private class OauthTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			SyncHttp syncHttp = new SyncHttp();
			String temp = null;
			try {
				temp = syncHttp.httpPost(params[0], Potocol.getOauthParams(
						Potocol.USER_NAME, Potocol.PASSWROD));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return temp;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("oauth----------->" + result);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_icon:
			getSlidingMenu().toggle();
			break;
		case R.id.personCenter:
			getSlidingMenu().toggle();
			break;
		default:
			break;
		}
	}

}
