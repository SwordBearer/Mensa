package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionArticle extends BaseBean {
	private static final long serialVersionUID = 1L;

	private int qId;
	private String qtitle;
	private String qcontent;
	private String qdate;
	private String qauthor;

	private String acontent;
	private String aauthor;
	private String adate;

	public QuestionArticle(JSONObject jo) throws JSONException {
		this.qId = jo.getInt("id");
		this.qtitle = jo.getString("qtitle");
		this.qcontent = jo.getString("qcontent");
		this.qdate = jo.getString("qdate");
		this.qauthor = jo.getString("qauthor");
		this.acontent = jo.getString("acontent");
		this.adate = jo.getString("adate");
		this.aauthor = jo.getString("aauthor");
	}

	public int getqId() {
		return qId;
	}

	public String getQtitle() {
		return qtitle;
	}

	public String getQcontent() {
		return qcontent;
	}

	public String getQdate() {
		return qdate;
	}

	public String getQauthor() {
		return qauthor;
	}

	public String getAcontent() {
		return acontent;
	}

	public String getAauthor() {
		return aauthor;
	}

	public String getAdate() {
		return adate;
	}
}
