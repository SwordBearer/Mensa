package com.mensa.fragment;

import com.mensa.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarketPage extends HomeBasePage {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_market, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {}
}
