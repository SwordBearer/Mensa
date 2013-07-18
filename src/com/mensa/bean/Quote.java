package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Quote extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String title;
	private double price;
	private double changeAmount;
	private double changePrice;

	private Quote() {}

	public static Quote parseJSON(JSONObject jo) throws JSONException {
		Quote quote = new Quote();
		quote.title = jo.getString("title");
		quote.price = jo.getDouble("price");
		quote.changeAmount = jo.getDouble("change_amount");
		quote.changePrice = jo.getDouble("change_price");
		return quote;
	}

	public String getTitle() {
		return title;
	}

	public double getPrice() {
		return price;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public double getChangePrice() {
		return changePrice;
	}
}
