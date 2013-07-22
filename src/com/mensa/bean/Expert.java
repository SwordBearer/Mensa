package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.mensa.application.MensaAppliaction;

public class Expert extends BaseBean {
	private static final long serialVersionUID = 1L;

	private int id = 1;
	private String img;
	private String name;
	private String position;
	private String desc;
	private String phone;

	public Expert() {}

	public Expert(JSONObject jo) throws JSONException {
		if (jo.has("id"))
			this.id = jo.getInt("id");
		else
			this.id = MensaAppliaction.getAppInfo().getExpertId();
		this.name = jo.getString("name");
		this.position = jo.getString("position");
		this.desc = jo.getString("description");
		if (jo.has("phone"))
			this.phone = jo.getString("phone");
		this.img = jo.getString("img");
	}

	public int getId() {
		return id;
	}

	public String getImg() {
		return img;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public String getDesc() {
		return desc;
	}

	public String getPhone() {
		return phone;
	}
}
