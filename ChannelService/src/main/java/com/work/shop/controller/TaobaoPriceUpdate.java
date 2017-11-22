package com.work.shop.controller;

public class TaobaoPriceUpdate extends UpdatePriceApi {

	@Override
	public String updateprice(String sn, double price) {
		
		String url = "http://api.taobao.com?sn=" + sn + "&price=" + price;
		
		return null;
	}

}
