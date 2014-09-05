package com.xiaowu.blogclient.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

/**
 * 文件工具类
 * 
 * @author wwj_748
 */
public class FileUtil {
	// 文件保存路径
	public static String filePath = android.os.Environment
			.getExternalStorageDirectory() + "/WWJBlog";

	public static String getFileName(String str) {
		// 去除url中的符号作为文件名返回
		str = str.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
		System.out.println("filename = " + str);
		return str + ".png";
	}

	/**
	 * 保存文件到SD卡中
	 * 
	 * @param filename
	 *            文件名
	 * @param inputStream
	 *            输入流
	 */
	public static void writeSDCard(String filename, InputStream inputStream) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(filePath
					+ "/" + filename);
			byte[] buffer = new byte[512];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, count);// 写入缓冲区
			}
			fileOutputStream.flush();// 写入文件
			fileOutputStream.close();// 关闭文件输出流
			inputStream.close();
			System.out.println("save success");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("save fail");
		}
	}

	public static boolean writeSDCard(String fileName, Bitmap bmp) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			InputStream is = bitmap2InputStream(bmp);

			FileOutputStream fileOutputStream = new FileOutputStream(filePath
					+ "/" + getFileName(fileName));
			byte[] buffer = new byte[512];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, count);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Bitmap转换为byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Bitmap转换成InputStream
	 * 
	 * @param bm
	 * @return
	 */
	public static InputStream bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
}
