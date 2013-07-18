package com.mensa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter extends BaseAdapter {
	protected List<?> mData;
	protected LayoutInflater mInflater;

	public BaseListAdapter(Context context, List<?> data) {
		mInflater = LayoutInflater.from(context);
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
