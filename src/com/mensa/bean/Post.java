package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Post extends News {
	private static final long serialVersionUID = 1L;

	public Post(JSONObject jo) throws JSONException {
		super(jo);
	}

	@Override
	public String getCacheKey() {
		return "mensa_post_details_" + getId();
	}
}
