package com.work.shop.api.mls.vo.item.itemGet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MLSItemColor {

	@JsonProperty(value="10001")
	private MLSItemColorDetail colorDetail;

	public MLSItemColorDetail getColorDetail() {
		return colorDetail;
	}

	public void setColorDetail(MLSItemColorDetail colorDetail) {
		this.colorDetail = colorDetail;
	}

	

	
}
