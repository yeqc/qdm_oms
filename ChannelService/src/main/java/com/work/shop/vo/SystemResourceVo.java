package com.work.shop.vo;

import java.util.List;

import com.work.shop.bean.SystemResource;

public class SystemResourceVo extends SystemResource {

	private List<SystemResource> childMenuList;

	public List<SystemResource> getChildMenuList() {
		return childMenuList;
	}

	public void setChildMenuList(List<SystemResource> childMenuList) {
		this.childMenuList = childMenuList;
	}

}
