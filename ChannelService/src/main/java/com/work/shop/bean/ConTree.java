package com.work.shop.bean;

public class ConTree {
	
	private String id;
	
	private String name;
	
	private String url;
	
	private String iconCls;
	
	private String level;
	
	private boolean leaf;
	
	public ConTree(String id, String name, String url, String iconCls, String level, boolean leaf) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.iconCls = iconCls;
		this.level = level;
		this.leaf = leaf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
}
