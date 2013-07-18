package com.mensa.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mensa.R;

public class UIHelper {
	public static void showToast(Context context, int msg, int type) {
		if (context == null)
			return;
		Toast toast = new Toast(context);
		View view = new View(context);
		if (type == 1) {
			view.setBackgroundResource(R.drawable.toast_info_bg);
			toast.setDuration(Toast.LENGTH_SHORT);
		} else {
			view.setBackgroundResource(R.drawable.toast_error_bg);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.setView(view);
		toast.setText(msg);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
