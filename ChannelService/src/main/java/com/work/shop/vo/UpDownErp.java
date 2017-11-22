package com.work.shop.vo;

public class UpDownErp {

	private String channel_code;
	
	private String sn;
	
	private Integer status;

	public UpDownErp() {
	}

	public UpDownErp(String channel_code, String sn, Integer status) {
		this.channel_code = channel_code;
		this.sn = sn;
		this.status = status;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
