package com.mensa.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

/**
 * 缓存工具类
 * 
 * @author swordbearer
 */
public class CacheUtil {
	public static final String KEY_NEWS = "lips_cache_news";
	private static final String TAG = "CacheUtil";

	/**
	 * 保存缓存文件：建议缓存的数据是可序列化的，便于读取时进行反序列化操作
	 * 
	 * @param context
	 * @param key缓存文件名
	 * @param data可以是Serializable对象
	 * @return
	 */
	public static boolean saveCache(Context context, String key, Object data) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File file = context.getFileStreamPath(key);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = context.openFileOutput(key, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

	/**
	 * 读取缓存文件
	 * 
	 * @param context
	 * @param key文件名
	 * @return 返回Object对象:如果返回的实体可以被反序列化，可以使用Serializable进行类型转换
	 */
	public static Object readCache(Context context, String key) {
		File data = context.getFileStreamPath(key);
		Log.e("TEST", "缓存文件名是 " + data.getAbsolutePath());
		if (!data.exists()) {
			return null;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(key);
			ois = new ObjectInputStream(fis);
			try {
				return ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除缓存
	 * 
	 * @param context
	 * @param key 缓存文件名
	 */
	public static void deleteCache(Context context, String key) {
		File cache = context.getFileStreamPath(key);
		if (cache.exists()) {
			cache.delete();
		}
	}

	public static String saveToFile(Context context, String url, InputStream inputStream) {
		String fileName = MD5Util.MD5Encode(url);// 加密后的文件名
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (IOException e2) {
			}
		}
		return context.getFilesDir() + "/" + fileName;
	}
}
