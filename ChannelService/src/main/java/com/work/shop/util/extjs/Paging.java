package com.work.shop.util.extjs;

import java.util.List;

/**
 * extjs Json格式的分页用数据
 * @author lhj
 *
 */
public class Paging {
	
	/**总记录数*/
	private int totalProperty;
	
	/** 当前页数据 */
	private List<?> root;
	
	/** 提示信息*/
	private String message;
	
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Paging() {
	}

	public Paging(int totalProperty, List<?> root) {
		super();
		this.totalProperty = totalProperty;
		this.root = root;
	}

	/**
	 * @return the totalProperty
	 */
	public int getTotalProperty() {
		return totalProperty;
	}

	/**
	 * @param totalProperty the totalProperty to set
	 */
	public void setTotalProperty(int totalProperty) {
		this.totalProperty = totalProperty;
	}

	/**
	 * @return the root
	 */
	public List<?> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(List<?> root) {
		this.root = root;
	}
	
	
}
