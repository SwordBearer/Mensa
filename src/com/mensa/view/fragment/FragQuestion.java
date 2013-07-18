package com.mensa.view.fragment;

import com.mensa.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragQuestion extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_question, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		// TODO Auto-generated method stub

	}

}
