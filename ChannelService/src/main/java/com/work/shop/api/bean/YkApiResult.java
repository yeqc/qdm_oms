package com.work.shop.api.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class YkApiResult <T> {

	@JSONField(name="PageCount")
	private Integer pageCount;
	@JSONField(name="Sign")
	private String sign;
	@JSONField(name="Status")
	private Boolean status;
	@JSONField(name="StatusCode")
	private Integer statusCode;
	@JSONField(name="Msg")
	private String msg;
	@JSONField(name="Timestamp")
	private String timestamp;
	@JSONField(name="Result")
	public T result;
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
}
