package com.mensa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.bean.Quote;

public class QuotesAdapter extends BaseListAdapter {

	private ArrayList<String> chineseMarket = new ArrayList<String>();
	int COLOR_GREEN, COLOR_RED;

	public QuotesAdapter(Context context, List<?> data) {
		super(context, data);
		COLOR_GREEN = context.getResources().getColor(R.color.quote_green);
		COLOR_RED = context.getResources().getColor(R.color.quote_red);

		chineseMarket.add(context.getString(R.string.chinese_m_1));
		chineseMarket.add(context.getString(R.string.chinese_m_2));
		chineseMarket.add(context.getString(R.string.chinese_m_3));
		chineseMarket.add(context.getString(R.string.chinese_m_4));
		chineseMarket.add(context.getString(R.string.chinese_m_5));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_quote, null, false);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.quote_item_title);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.quote_item_price);
			holder.tvChangeAmount = (TextView) convertView.findViewById(R.id.quote_item_changeA);
			holder.tvChangePrice = (TextView) convertView.findViewById(R.id.quote_item_changeP);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Quote quote = (Quote) mData.get(position);
		String title = quote.getTitle();
		double amount = quote.getChangeAmount();
		holder.tvTitle.setText(quote.getTitle());
		holder.tvPrice.setText(quote.getPrice() + "");
		holder.tvChangeAmount.setText(amount + "");
		holder.tvChangePrice.setText(quote.getChangePrice() + "");
		int color = Color.RED;
		// 中国市场 红涨绿跌
		if (chineseMarket.indexOf(title) >= 0) {
			if (amount > 0) {
				color = COLOR_RED;
			} else {
				color = COLOR_GREEN;
			}
		} else {// 非中国市场 绿涨红跌
			if (amount > 0) {
				color = COLOR_GREEN;
			} else {
				color = COLOR_RED;
			}
		}
		holder.tvChangeAmount.setTextColor(color);
		holder.tvChangePrice.setTextColor(color);
		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		TextView tvPrice;
		TextView tvChangeAmount;
		TextView tvChangePrice;
	}

}
