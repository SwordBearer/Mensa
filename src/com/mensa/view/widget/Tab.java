package com.mensa.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.mensa.R;

public class Tab extends RadioButton {
	private int _textColorOn, _textColorOff;

	protected Tab(Context context) {
		super(context);
		init(context, null);
	}

	public Tab(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public Tab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TabButton);
			_textColorOn = typedArray.getColor(R.styleable.TabButton_textColorOn, R.color.black);
			_textColorOff = typedArray.getColor(R.styleable.TabButton_textColorOff, R.color.gray);
			typedArray.recycle();
		} else {
			_textColorOn = R.color.black;
			_textColorOff = R.color.gray;
		}
		setClickable(true);
		if (this.getText() == null || getText().equals("")) {
			setTextSize(0);
		}
		setGravity(Gravity.CENTER);
		this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					setTextColor(_textColorOn);
				} else
					setTextColor(_textColorOff);
			}
		});
	}
}