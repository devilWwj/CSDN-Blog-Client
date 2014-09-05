package com.xiaowu.blogclient.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class MySharedPreferences {

	public MySharedPreferences(Activity activity) {
	}

	/**
	 * 读取对应的键值
	 * 
	 * @param key
	 * @return String
	 */
	public static int readMessage(Activity activity, String key, int value) {
		// 获得当前的SharedPreferences对象
		SharedPreferences message = activity
				.getPreferences(Activity.MODE_PRIVATE);
		// 获取消息
		int tmp = message.getInt(key, value);
		return tmp;
	}

	/**
	 * 将键值对写入配置文件
	 * 
	 * @param key
	 * @param value
	 */
	public static void writeMessage(Activity activity, String key, int value) {
		// 创建一个SharedPreferences对象
		SharedPreferences message = activity.getPreferences(0);
		// 编辑SharedPreferences对象
		SharedPreferences.Editor editor = message.edit();
		// 插入一个数据
		editor.putInt(key, value);
		// 提交数据
		editor.commit();
	}

	public static String readMessage(Activity activity, String key, String value) {
		SharedPreferences message = activity.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		String text = message.getString(key, value);
		return text;
	}

	public static void writeMessage(Activity activity, String key, String value) {
		SharedPreferences message = activity.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = message.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
