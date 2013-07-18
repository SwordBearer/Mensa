package com.mensa.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	public static final int MSG_OK = 0x88;
	public static final int MSG_ERROR = 0x89;

	protected Context mContext;

	public abstract void initViews(View rootView);

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

}
