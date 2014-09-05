package com.xiaowu.blogclient.umeng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * @功能描述 : 在此进行平台的配置， 比如需要支持QQ平台，则添加相应的项即可
 *
 * @原 作 者 : JhenXu
 * @版 本 号 : [版本号, Aug 4, 2013]
 *
 * @修 改 人 : mrsimple
 * @修改内容 : 添加QQ支持
 */
public class SocializeConfigDemo {
	public static boolean SUPPORT_SINA = true;
	public static boolean SUPPORT_RENR = true;
	public static boolean SUPPORT_DOUBAN = true;
	public static boolean SUPPORT_QZONE = true;
	public static boolean SUPPORT_TENC = true;

	public static boolean SUPPORT_FACEBOOK = false;
	public static boolean SUPPORT_TWITTER = false;
	public static boolean SUPPORT_GOOGLE = false;

	public static final String DESCRIPTOR = "com.umeng.share";

	private static final Set<WeakReference<SocializeConfig>> wCigs = new HashSet<WeakReference<SocializeConfig>>();

	/**
	 * demo 中需要和侧边栏配置联动，所以使用代理方式获取Config 实例。
	 * 
	 * @return
	 */
	public final static SocializeConfig getSocialConfig(Context context) {
		SocializeConfig config = SocializeConfig.getSocializeConfig();
		WeakReference<SocializeConfig> ref = new WeakReference<SocializeConfig>(
				config);
		wCigs.add(ref);

		List<SHARE_MEDIA> supportMedias = new ArrayList<SHARE_MEDIA>();
		if (SUPPORT_QZONE) {
			supportMedias.add(SHARE_MEDIA.QZONE);
		}
		if (SUPPORT_SINA) {
			supportMedias.add(SHARE_MEDIA.SINA);
		}
		if (SUPPORT_TENC) {
			supportMedias.add(SHARE_MEDIA.TENCENT);
		}
		if (SUPPORT_RENR) {
			supportMedias.add(SHARE_MEDIA.RENREN);
		}
		if (SUPPORT_DOUBAN) {
			supportMedias.add(SHARE_MEDIA.DOUBAN);
		}

		config.supportAppPlatform(context, SHARE_MEDIA.FACEBOOK, DESCRIPTOR,
				SUPPORT_FACEBOOK);
		config.supportAppPlatform(context, SHARE_MEDIA.TWITTER, DESCRIPTOR,
				SUPPORT_TWITTER);
		config.supportAppPlatform(context, SHARE_MEDIA.GOOGLEPLUS, DESCRIPTOR,
				SUPPORT_GOOGLE);

		return config;
	}

	public synchronized final static void nofifyConfigChange(Context context) {
		Set<WeakReference<SocializeConfig>> deltable = new HashSet<WeakReference<SocializeConfig>>();
		for (WeakReference<SocializeConfig> ref : wCigs) {
			SocializeConfig cig = ref.get();
			if (cig != null) {
				cig.supportAppPlatform(context, SHARE_MEDIA.FACEBOOK,
						DESCRIPTOR, SUPPORT_FACEBOOK);
				cig.supportAppPlatform(context, SHARE_MEDIA.TWITTER,
						DESCRIPTOR, SUPPORT_TWITTER);
				cig.supportAppPlatform(context, SHARE_MEDIA.GOOGLEPLUS,
						DESCRIPTOR, SUPPORT_GOOGLE);
			} else
				deltable.add(ref);
		}

		for (WeakReference<SocializeConfig> ref : deltable) {
			if (wCigs.contains(ref))
				wCigs.remove(ref);
		}

		UMSocialService umSocialService = UMServiceFactory
				.getUMSocialService("no private config");
		SocializeConfig config = umSocialService.getConfig();
		umSocialService.setGlobalConfig(config);

	}

	public static final SHARE_MEDIA[] getSupportPlatforms() {
		List<SHARE_MEDIA> lists = new ArrayList<SHARE_MEDIA>();
		if (SUPPORT_QZONE) {
			lists.add(SHARE_MEDIA.QZONE);
		}
		if (SUPPORT_SINA) {
			lists.add(SHARE_MEDIA.SINA);
		}
		if (SUPPORT_TENC) {
			lists.add(SHARE_MEDIA.TENCENT);
		}
		if (SUPPORT_RENR) {
			lists.add(SHARE_MEDIA.RENREN);
		}
		if (SUPPORT_DOUBAN) {
			lists.add(SHARE_MEDIA.DOUBAN);
		}

		return lists.toArray(new SHARE_MEDIA[lists.size()]);

	}

}
