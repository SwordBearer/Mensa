package com.mensa.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.bean.Expert;
import com.mensa.view.widget.AsyncImageView;

public class ExpertAdapter extends BaseListAdapter {
	public ExpertAdapter(Context context, List<?> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_expert, null, false);
			holder.img = (AsyncImageView) convertView.findViewById(R.id.expert_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.expert_name);
			holder.tvPos = (TextView) convertView.findViewById(R.id.expert_position);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.expert_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Expert expert = (Expert) mData.get(position);
		Log.e("ExpertAdapter ", expert.getName());
		try {
			holder.img.loadImage(expert.getImg());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		holder.tvName.setText(expert.getName());
		holder.tvPos.setText(expert.getPosition());
		holder.tvDesc.setText(expert.getDesc());
		return convertView;
	}

	private class ViewHolder {
		AsyncImageView img;
		TextView tvName, tvPos, tvDesc;
	}
}
