package com.work.shop.bean;

public class ColorAndSize {

	/**
	 * 颜色名称
	 ***/
	private String colorName;
	
	/**
	 * 颜色码
	 ***/
	private String colorCode;
	
	private String sizeCode;
	private String sizeName;
	
	public String getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
