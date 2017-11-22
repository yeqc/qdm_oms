package com.work.shop.vo;

import com.work.shop.bean.ChannelTemplateContent;

public class ChannelTemplateContentVo extends ChannelTemplateContent {

	private String tbModuleName;

	private String moduleName;

	public String getTbModuleName() {
		return tbModuleName;
	}

	public void setTbModuleName(String tbModuleName) {
		this.tbModuleName = tbModuleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
