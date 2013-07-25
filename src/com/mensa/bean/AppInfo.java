package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class AppInfo extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String ver;
	private String sms;
	private int expertId = 1;
	private int type;
	private String url;
	private String info;
	private String bb_img;
	private String bb_url;
	private String subdomain;
	private String webtitle;

	public AppInfo() {
		this.ver = "1.3";
		this.sms = "";
		this.expertId = 1;
		this.type = 1;
		this.url = "";
		this.info = "";
		this.bb_img = "";
		this.bb_url = "";
		this.subdomain = "";
		this.webtitle = "Mensa 投资";

	}

	public static AppInfo parseJSON(JSONObject jo) {
		AppInfo appInfo = new AppInfo();
		try {
			if (jo.has("sms"))
				appInfo.sms = jo.getString("sms");
			if (jo.has("cid"))
				appInfo.expertId = jo.getInt("cid");
			if (jo.has("type"))
				appInfo.type = jo.getInt("type");
			if (jo.has("url"))
				appInfo.url = jo.getString("url");
			if (jo.has("ver"))
				appInfo.ver = jo.getString("ver");
			if (jo.has("info"))
				appInfo.info = jo.getString("info");
			if (jo.has("bb_img"))
				appInfo.bb_img = jo.getString("bb_img");
			if (jo.has("bb_url"))
				appInfo.bb_url = jo.getString("bb_url");
			if (jo.has("subdomain"))
				appInfo.subdomain = jo.getString("subdomain");
			if (jo.has("webtitle"))
				appInfo.webtitle = jo.getString("webtitle");
		} catch (JSONException e) {
		}
		return appInfo;
	}

	public String getVer() {
		return ver;
	}

	public String getSms() {
		return sms;
	}

	public int getExpertId() {
		return expertId;
	}

	public int getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getInfo() {
		return info;
	}

	public String getBb_img() {
		return bb_img;
	}

	public String getBb_url() {
		return bb_url;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public String getWebtitle() {
		return webtitle;
	}
}
