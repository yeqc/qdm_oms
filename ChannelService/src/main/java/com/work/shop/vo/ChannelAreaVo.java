package com.work.shop.vo;

import com.work.shop.bean.SystemArea;

public class ChannelAreaVo extends SystemArea {
	
	private String regionId;
	private String regionName;
	private String areaStatus; //映射状态作为查询条件，数据库不存在该字段
	
	
	public String getAreaStatus() {
		return areaStatus;
	}
	public void setAreaStatus(String areaStatus) {
		this.areaStatus = areaStatus;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	

}
