package com.work.shop.api.bean;


public class ApiResultVO <T> {
	
	/** 返回码 1 接口成功返回 **/
	private String code;
	
	private String message;
	
	private Integer total;
	
	private T apiGoods;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getApiGoods() {
		return apiGoods;
	}

	public void setApiGoods(T apiGoods) {
		this.apiGoods = apiGoods;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public ApiResultVO(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ApiResultVO() {
		super();
	}
}
