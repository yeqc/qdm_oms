package com.work.shop.vo;

import com.work.shop.bean.SystemRegionMatch;

public class ChannelAreaMatchVo extends SystemRegionMatch {
	
	private String oldRegionId;
	private String oldRegionName;
	private String oldRegionType;
	
	public String getOldRegionId() {
		return oldRegionId;
	}
	public void setOldRegionId(String oldRegionId) {
		this.oldRegionId = oldRegionId;
	}
	public String getOldRegionName() {
		return oldRegionName;
	}
	public void setOldRegionName(String oldRegionName) {
		this.oldRegionName = oldRegionName;
	}
	public String getOldRegionType() {
		return oldRegionType;
	}
	public void setOldRegionType(String oldRegionType) {
		this.oldRegionType = oldRegionType;
	}
}
