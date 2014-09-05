package com.xiaowu.blogclient.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
				Locale.CHINA);
		return sdf.format(new java.util.Date());
	}
}
