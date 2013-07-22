package com.mensa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.bean.News;

public class NewsAdapter extends BaseListAdapter {

	public NewsAdapter(Context context, List<?> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_news, null, false);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.news_item_title);
			holder.tvInfo = (TextView) convertView.findViewById(R.id.news_item_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		News news = (News) mData.get(position);
		holder.tvTitle.setText(news.getTitle() + "");
		holder.tvInfo.setText(news.getAuthor() + "  " + news.getDate());
		return convertView;
	}

	class ViewHolder {
		public TextView tvTitle;
		public TextView tvInfo;
	}
}
