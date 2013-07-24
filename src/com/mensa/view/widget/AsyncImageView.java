package com.mensa.view.widget;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.mensa.net.CacheUtil;
import com.mensa.net.MD5Util;
import com.mensa.net.NetHelper;

/**
 * deprecated:一部加载图片控件
 * 
 * @author swordbearer
 */
public class AsyncImageView extends ImageView {
	// 防止重复下载
	private boolean isRunning = false;

	private Bitmap mBitmap;

	public AsyncImageView(Context context) {
		super(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void loadImage(String url) {
		String filePath = getContext().getFilesDir() + "/" + MD5Util.MD5Encode(url);
		Bitmap bmp = BitmapFactory.decodeFile(filePath);
		if (bmp != null) {
			this.setImageBitmap(bmp);
			return;
		}
		if (!isRunning) {
			new AsyncImageLoader().execute(url);
		}
	}

	private class AsyncImageLoader extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			InputStream is = NetHelper.httpGet(params[0]);
			String filePath = CacheUtil.saveToFile(getContext(), params[0], is);
			Log.e("AsyncImageView", "saveToFile  " + filePath);
			return BitmapFactory.decodeFile(filePath);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			AsyncImageView.this.setImageBitmap(bitmap);
			mBitmap = bitmap;
			isRunning = false;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {}

		@Override
		protected void onCancelled() {
			isRunning = false;
		}
	}
}
