package com.mensa.view.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.adapter.PostAdapter;
import com.mensa.bean.Expert;
import com.mensa.bean.Post;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.NewsDetailsActivity;
import com.mensa.view.UIHelper;
import com.mensa.view.widget.AsyncImageView;

/**
 * 专栏页面
 * 
 * @author SwordBearer
 * 
 */
public class FragSpecialColumn extends BaseFragment {
	private static final int MSG_OK = 0x31;
	private static final int MSG_ERROR = 0x32;
	String TAG = "FragSpecialColumn";
	private Expert mExpert = new Expert();
	private List<Post> posts = new ArrayList<Post>();

	private AsyncImageView imageView;
	private TextView tvName, tvPosition, tvDesc;
	private Button btnPhone;
	private ListView lvPosts;
	private PostAdapter postAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_specialcolumn, null, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		imageView = (AsyncImageView) rootView.findViewById(R.id.frag_expert_image);
		tvName = (TextView) rootView.findViewById(R.id.frag_expert_name);
		tvPosition = (TextView) rootView.findViewById(R.id.frag_expert_position);
		tvDesc = (TextView) rootView.findViewById(R.id.frag_expert_desc);
		btnPhone = (Button) rootView.findViewById(R.id.frag_expert_phone);
		lvPosts = (ListView) rootView.findViewById(R.id.frag_expert_lv);

		btnPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = mExpert.getPhone();
				if (phone == null || phone.equals(""))
					return;
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mExpert.getPhone()));
				startActivity(intent);
			}
		});
		lvPosts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent detailsIntent = new Intent(getActivity(), NewsDetailsActivity.class);
				detailsIntent.putExtra("extra_news", posts.get(position));
				startActivity(detailsIntent);
			}
		});
		postAdapter = new PostAdapter(mContext, posts);
		lvPosts.setAdapter(postAdapter);
		//
		loadData();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		if (!NetHelper.isNetworkConnected(mContext)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				NetHelper.getExpert(mExpert.getId(), loadExportListener);
				NetHelper.getPosts(loadPostsListener);
			}
		}).start();
	}

	private void loadExpertImage() {
		String img = mExpert.getImg();
		if (img == null || img.equals(""))
			return;
		if (!NetHelper.isNetworkConnected(mContext))
			return;
//		try {
			imageView.loadImage(img);
//		} catch (Exception ex) {
		// ex.printStackTrace();
		// }
	}

	/**
	 * 更新界面
	 */
	private void updateViews() {
		// 更新上半部分
		if (mExpert.getName() != null && !mExpert.getName().equals("")) {
			tvName.setText(mExpert.getName());
			tvPosition.setText(mExpert.getPosition());
			tvDesc.setText(mExpert.getDesc());
			String phone = mExpert.getPhone();
			if (phone == null || phone.equals("")) {
				btnPhone.setVisibility(View.INVISIBLE);
				return;
			}
			btnPhone.setText(phone);
			btnPhone.setVisibility(View.VISIBLE);
			btnPhone.setClickable(true);
		}
		// 更新文章列表
		if (posts.size() > 0) {
			// postAdapter.notifyDataSetChanged();
			postAdapter = new PostAdapter(mContext, posts);
			lvPosts.setAdapter(postAdapter);
			Log.e(TAG, "更新Posts列表 " + postAdapter.getCount() + "---" + posts.size() + lvPosts.getCount());
		}
	}

	private OnRequestListener loadExportListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {}

		@Override
		public void onComplete(Object object) {
			try {
				JSONObject jo = new JSONObject(object.toString());
				mExpert = new Expert(jo);
				handler.sendEmptyMessage(MSG_OK);
				loadExpertImage();
			} catch (JSONException e) {
				onError(null);
			}
		}
	};
	private OnRequestListener loadPostsListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			try {
				JSONArray ja = new JSONArray(object.toString());
				posts.clear();
				for (int i = 0; i < ja.length(); i++) {
					posts.add(new Post(ja.getJSONObject(i)));
				}
				handler.sendEmptyMessage(MSG_OK);
			} catch (JSONException e) {
				e.printStackTrace();
				onError(null);
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OK:
				updateViews();
				break;
			case MSG_ERROR:
				UIHelper.showToast(mContext, R.string.get_specialcolumn_error);
				break;
			}
		}
	};

}
