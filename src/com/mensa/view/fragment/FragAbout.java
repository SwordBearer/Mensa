package com.mensa.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mensa.R;

public class FragAbout extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_about, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {}

}
