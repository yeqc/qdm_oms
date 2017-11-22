package com.work.shop.api.bean;

import java.io.Serializable;


public class ReturnInfo<T> implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -5785052686949245384L;

	private String orderSn;// 订单编号
	private String orderOutSn;// 外部交易号
//	
	private String returnSn;//退单编号
	
	private String relatingOrderSn;//原订单号
//	
//	private List<String> paySn = new ArrayList<String>();// 付款单编号
//	
//	private List<String> shipSn =  new ArrayList<String>();// 发货单编号
	
	private int isOk;// 返回结果0：执行不成功；1：成功
	
	private String message;// 成功或失败信息
	
	private T data;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
//
//
//	public List<String> getPaySn() {
//		return paySn;
//	}
//
//	public void setPaySn(List<String> paySn) {
//		this.paySn = paySn;
//	}
//
//	public List<String> getShipSn() {
//		return shipSn;
//	}
//
//	public void setShipSn(List<String> shipSn) {
//		this.shipSn = shipSn;
//	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public ReturnInfo(int isOk, String message) {
		super();
		this.isOk = isOk;
		this.message = message;
	}
	public ReturnInfo() {
	}
	public ReturnInfo(int isOk) {
		super();
		this.isOk = isOk;
	}
	public int getIsOk() {
		return isOk;
	}

	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReturnInfo [orderSn=" + orderSn + ", orderOutSn=" + orderOutSn
				+ ", returnSn=" + returnSn + ", relatingOrderSn="
				+ relatingOrderSn + ", isOk=" + isOk + ", message=" + message
				+ ", data=" + data + "]";
	}
}
