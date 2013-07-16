package com.mensa.widget.tactionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

/**
 *
 */
public class TationBar extends RadioGroup {

	private static final String TAG = "TationBar";

	public TationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setClickable(true);
	}

	public Tab newTab(Context context, int resid) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return (Tab) inflater.inflate(resid, this, false);
	}

	/**
	 * 获取当前选中的RadioButton的index
	 * 
	 * @return
	 */
	public int getCheckedIndex() {
		int checkedIndex = -1;
		int count = this.getChildCount();
		for (int i = 0; i < count; i++) {
			Tab tab = (Tab) this.getChildAt(i);
			if (tab.isChecked()) {
				checkedIndex = i;
				Log.e(TAG, TAG + " checkedIndex " + i);
				break;
			}
		}
		return checkedIndex;
	}

	/**
	 * 设置此时的Tab
	 * 
	 * @param index
	 */
	public void setCurrentTab(int index) {
		Tab tab = (Tab) this.getChildAt(index);
		if (tab.isChecked())
			return;
		else {
			tab.setChecked(true);
		}
	}

}
