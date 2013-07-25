package com.mensa.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mensa.R;

public class UIHelper {
	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, int msg) {
		if (context == null)
			return;
		Toast toast = new Toast(context);
		TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
		view.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(Context context, String msg) {
		if (context == null)
			return;
		Toast toast = new Toast(context);
		TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
		view.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static PopupWindow createPopupList(Context context, View parent, String[] lists, OnItemClickListener listener) {
		PopupWindow popup = new PopupWindow(context);
		ListView lv = new ListView(context);
		lv.setCacheColorHint(android.R.color.transparent);
		lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, lists));
		lv.setOnItemClickListener(listener);
		popup.setContentView(lv);
		return popup;
	}

}
