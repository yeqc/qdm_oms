package com.work.shop.vo;

public class EditReturnResult {
	
	public boolean success = true;
	
	public String msg;

	public EditReturnResult() {
	}
	
	public EditReturnResult(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
