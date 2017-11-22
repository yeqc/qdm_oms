package com.work.shop.vo;

public class SearchResult implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code; // 0--成功，1--失败
	private String msg; // 返回处理信息
	private Object data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
