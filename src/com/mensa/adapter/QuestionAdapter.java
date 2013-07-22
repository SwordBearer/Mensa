package com.mensa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.bean.Question;

public class QuestionAdapter extends BaseListAdapter {

	public QuestionAdapter(Context context, List<?> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_question, null, false);
			holder = new ViewHolder();
			holder.tvTtile = (TextView) convertView.findViewById(R.id.question_item_title);
			holder.tvDate = (TextView) convertView.findViewById(R.id.question_item_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Question question = (Question) mData.get(position);
		holder.tvTtile.setText(question.getTitle());
		holder.tvDate.setText(question.getDate());
		return convertView;
	}

	class ViewHolder {
		TextView tvTtile, tvDate;
	}

}
