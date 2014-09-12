package com.xiaowu.blogclient;

import static com.xiaowu.blogclient.umeng.SocializeConfigDemo.DESCRIPTOR;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.diy.banner.DiyAdSize;
import net.youmi.android.diy.banner.DiyBanner;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaowu.blogclient.model.Blogger;
import com.xiaowu.blogclient.util.Constants;
import com.xiaowu.blogclient.util.HttpUtil;
import com.xiaowu.blogclient.util.JsoupUtil;
import com.xiaowu.blogclient.view.PullScrollView;

/**
 * 
 * 个人中心
 * 
 * @author wwj_748
 * 
 */
public class PersonCenterFragment extends Fragment implements OnClickListener,
		PullScrollView.OnTurnListener {
	private PullScrollView mScrollView;

	private TextView visitText; // 访问
	private TextView jifenText; // 积分
	private TextView rankText; // 排名
	private TextView originalText; // 原创
	private TextView transportText; // 转载
	private TextView translationText; // 翻译
	private TextView commentText; // 评论

	private ImageView mHeadImg;
	private View checkUpdate;
	private View about;
	private View share;
	private Button showSport;

	public static String URL = "http://blog.csdn.net/wwj_748";

	private Context mContext = null;
	// sdk controller
	private UMSocialService mController = null;
	// 要分享的文字内容
	private String mShareContent = "";
	// 要分享的图片
	private UMImage mUMImgBitmap = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_center, null);
		findViews(view);
		initConfig();
		initAd(view);

		new MainTask().execute(URL, Constants.DEF_TASK_TYPE.REFRESH);

		return view;
	}

	private void setUMUpdateListener() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					Toast.makeText(getActivity(), "发现更新", Toast.LENGTH_SHORT)
							.show();
					break;
				case UpdateStatus.No: // has noupdate
					Toast.makeText(getActivity(), "当前为最新版本", Toast.LENGTH_SHORT)
							.show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新",
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}

		});
	}

	/**
	 * 查找控件
	 * 
	 * @param view
	 */
	private void findViews(View view) {
		mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) view.findViewById(R.id.background_img);
		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);

		visitText = (TextView) view.findViewById(R.id.tv1);
		jifenText = (TextView) view.findViewById(R.id.tv2);
		rankText = (TextView) view.findViewById(R.id.tv3);
		originalText = (TextView) view.findViewById(R.id.tv4);
		transportText = (TextView) view.findViewById(R.id.tv5);
		translationText = (TextView) view.findViewById(R.id.tv6);
		commentText = (TextView) view.findViewById(R.id.tv7);

		checkUpdate = view.findViewById(R.id.checkUpdateView);
		about = view.findViewById(R.id.aboutView);
		share = view.findViewById(R.id.shareView);
		showSport = (Button) view.findViewById(R.id.showSpot);
		checkUpdate.setOnClickListener(this);
		about.setOnClickListener(this);
		share.setOnClickListener(this);
		showSport.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkUpdateView:// 手动强制更新
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.forceUpdate(getActivity());
			setUMUpdateListener();
			break;
		case R.id.aboutView: // 关于
			Intent intent = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.shareView: // 分享好友
			openShareBoard();
			break;
		case R.id.showSpot: // 展示插屏广播
			// 展示插播广告，可以不调用loadSpot独立使用
			SpotManager.getInstance(getActivity()).showSpotAds(getActivity(),
					new SpotDialogListener() {
						@Override
						public void onShowSuccess() {
							Log.i("YoumiAdDemo", "展示成功");
						}

						@Override
						public void onShowFailed() {
							Log.i("YoumiAdDemo", "展示失败");
						}

					});
			break;
		default:
			break;
		}
	}

	/**
	 * @功能描述 : 初始化与SDK相关的成员变量
	 */
	private void initConfig() {
		mContext = getActivity();
		mController = UMServiceFactory.getUMSocialService(DESCRIPTOR);

		// 要分享的文字内容
		mShareContent = "小巫CSDN博客客户端，CSDN移动开发专家——IT_xiao小巫的专属客户端，你值得拥有。";
		mController.setShareContent(mShareContent);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.xiaowu);

		mUMImgBitmap = new UMImage(mContext, bitmap);
		mController.setShareImage(mUMImgBitmap);
		mController.setAppWebSite(""); // 设置应用地址

		// 添加新浪和qq空间的SSO授权支持
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO支持
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx880cb2b22509cf25";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appID);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(mShareContent);
		// 设置title
		weixinContent.setTitle("小巫CSDN博客客户端");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl("你的http://blog.csdn.net/wwj_748链接");
		// 设置分享图片
		weixinContent.setShareImage(mUMImgBitmap);
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(mShareContent);
		// 设置朋友圈title
		circleMedia.setTitle("小巫CSDN博客客户端");
		circleMedia.setShareImage(mUMImgBitmap);
		circleMedia.setTargetUrl("你的http://blog.csdn.net/wwj_748链接");
		mController.setShareMedia(circleMedia);

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				"1102369913", "62ru775qbkentOUp");
		qqSsoHandler.addToSocialSDK();

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"1102369913", "62ru775qbkentOUp");
		qZoneSsoHandler.addToSocialSDK();

		// 添加人人网SSO授权功能
		// APPID:201874
		// API Key:28401c0964f04a72a14c812d6132fcef
		// Secret:3bf66e42db1e4fa9829b955cc300b737
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(),
				"271529", "682c45dbdeba4b608922fef124223efb",
				"2c7c3b63f58b4bfcad3665b49e65d47f");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();

		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(mShareContent);
		qqShareContent.setTitle("小巫CSDN博客");
		qqShareContent.setShareImage(mUMImgBitmap);
		qqShareContent.setTargetUrl("http://blog.csdn.net/wwj_748");
		mController.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(mShareContent);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl("http://blog.csdn.net/wwj_748");
		// 设置分享内容的标题
		qzone.setTitle("小巫CSDN博客");
		// 设置分享图片
		qzone.setShareImage(mUMImgBitmap);
		mController.setShareMedia(qzone);

	}

	/**
	 * 打开分享盘
	 */
	private void openShareBoard() {
		mController.openShare(getActivity(), false);

	}

	@Override
	public void onTurn() {
		new MainTask().execute(URL, Constants.DEF_TASK_TYPE.REFRESH);
	}

	/**
	 * 初始化广告
	 * @param view
	 */
	public void initAd(View view) {
		// 初始化接口，应用启动的时候调用
		// 参数：appId, appSecret, 调试模式
		AdManager.getInstance(getActivity()).init("8df70b90ebf86823",
				"b7659d08439c052b", false);
//		// 广告条接口调用（适用于应用）
//		// 将广告条adView添加到需要展示的layout控件中
//		LinearLayout adLayout = (LinearLayout) view.findViewById(R.id.adLayout);
//		AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
//		adLayout.addView(adView);
		
		//普通布局，适用于应用
        //获取要嵌入迷你广告条的布局
        RelativeLayout adLayout =(RelativeLayout)view.findViewById(R.id.adLayout);
        //demo 1 迷你Banner : 宽满屏，高32dp
        DiyBanner banner = new DiyBanner(getActivity(), DiyAdSize.SIZE_MATCH_SCREENx32);//传入高度为32dp的AdSize来定义迷你Banner    
        //demo 2 迷你Banner : 宽320dp，高32dp
        //DiyBanner banner = new DiyBanner(this, DiyAdSize.SIZE_320x32);//传入高度为32dp的AdSize来定义迷你Banner 
        //将积分Banner加入到布局中
        adLayout.addView(banner);

		// 监听广告条接口
//		adView.setAdListener(new AdViewListener() {
//
//			@Override
//			public void onSwitchedAd(AdView arg0) {
//				Log.i("YoumiAdDemo", "广告条切换");
//			}
//
//			@Override
//			public void onReceivedAd(AdView arg0) {
//				Log.i("YoumiAdDemo", "请求广告成功");
//			}
//
//			@Override
//			public void onFailedToReceivedAd(AdView arg0) {
//				Log.i("YoumiAdDemo", "请求广告失败");
//			}
//		});

		// 插播接口调用
		// 开发者可以到开发者后台设置展示频率，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）
		// 自4.03版本增加云控制是否开启防误点功能，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）

		// 加载插播资源
		SpotManager.getInstance(getActivity()).loadSpotAds();
		// 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
		SpotManager.getInstance(getActivity()).setSpotTimeout(5000);// 设置5秒
		SpotManager.getInstance(getActivity()).setShowInterval(20);// 设置20秒的显示时间间隔
		// 如需要使用自动关闭插屏功能，请取消注释下面方法
		SpotManager.getInstance(getActivity()).setAutoCloseSpot(true);// 设置自动关闭插屏开关
		SpotManager.getInstance(getActivity()).setCloseTime(6000); // 设置关闭插屏时间

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private class MainTask extends AsyncTask<String, Void, Blogger> {

		@Override
		protected Blogger doInBackground(String... params) {
			String temp = HttpUtil.httpGet(params[0]);
			Blogger blogger = JsoupUtil.getBloggerInfo(temp);
			return blogger;
		}

		@Override
		protected void onPostExecute(Blogger result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			String[] rank = result.getRank().split("\\|");
			String visitNum = rank[0];
			String jifenNum = rank[1];
			String rankNum = rank[2];

			String[] statics = result.getStatistics().split("\\|");
			String originalNum = statics[0];
			String transportNum = statics[1];
			String translationNum = statics[2];
			String commentNum = statics[3];
			visitText.setText(visitNum);
			jifenText.setText(jifenNum);
			rankText.setText(rankNum);
			originalText.setText(originalNum);
			transportText.setText(transportNum);
			translationText.setText(translationNum);
			commentText.setText(commentNum);
		}
	}

	@Override
	public void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(getActivity()).disMiss(false);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// 取消注册监听
		SpotManager.getInstance(getActivity()).unregisterSceenReceiver();
		super.onDestroy();
	}

}
