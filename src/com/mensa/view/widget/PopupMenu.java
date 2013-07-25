package com.mensa.view.widget;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mensa.R;

public class PopupMenu extends PopupWindow {
	private Context context;

	public PopupMenu(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 设置弹出菜单的选项，背景，长宽
	 * 
	 * @param itemsId菜单项数组的ID
	 * @param itemLayoutId一行布局的ID
	 * @param bgDrawableId背景ID
	 * @param width菜单宽度
	 * @param height菜单高度
	 */
	public void setWindow(String[] items, int width, int height, OnItemClickListener listener) {
		ListView lv = new ListView(context);
		lv.setCacheColorHint(android.R.color.transparent);
		lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items));
		lv.setOnItemClickListener(listener);
		setContentView(lv);
		setFocusable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popwindow_menu));
		setWidth(200);
		setHeight(height);
		update();
	}
}