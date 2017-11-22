package com.work.shop.controller;

public abstract class UpdatePriceApi {
	
	
	private static final String TAOBAO_CHANNEL_CODE = "1";
	private static final String JD_CHANNEL_CODE = "2";
	
	/*
	 * 
	 * <bean id="1" class="com.mb.taobaoapi">
	 * */
	
	public abstract String updateprice(String sn,double price);
		
}
