package com.xiaowu.blogclient.adapter;

import com.xiaowu.blogclient.BlogFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * FragmentPager适配器
 * 
 * @author wwj_748
 * @2014/8/9
 */
public class TabAdapter extends FragmentPagerAdapter {
	// 内容标题
	// public static final String[] TITLE = new String[] {
	// "【Android新浪微博客户端开发】", "【Android应用-小巫新闻客户端】",
	// "【Android应用-简、美音乐播放器】", "【Android多媒体开发系列】", "【Android UI效果锦集】",
	// "【Android开发学习之路】", "【Android开发记录】", "【Android SDK开发】",
	// "【Android设计模式系列】", "【Android通讯录模块开发】", "【Android自定义组件】",
	// "【Android Design翻译】", "【cocos2d-x】", "【2013年求职之路-小巫的面试宝典】",
	// "【Lua脚本语言】", "【技能提升之设计模式】", "【成长记录之记录点滴】", "【网络协议】", "【Go语言程序设计】",
	// "【IPhone开发】", "【PHP服务端】", "【Unity 3D】", "【建站经验】" };

	public static final String[] TITLE = new String[] { "首页", "Android",
			"cocos2d-x", "面试宝典", "Lua", "设计模式", "记录点滴", "网络协议", "Go语言", "建站经验" };

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	// 获取项
	@Override
	public Fragment getItem(int position) {
		System.out.println("Fragment position:" + position);
		switch (position) {
		case 0:
			break;
		default:
			break;
		}
		return new BlogFrag(position);
		// MainFragment fragment = new MainFragment(position);
		// return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// 返回页面标题
		return TITLE[position % TITLE.length].toUpperCase();
	}

	@Override
	public int getCount() {
		// 页面个数
		return TITLE.length;
	}

}
