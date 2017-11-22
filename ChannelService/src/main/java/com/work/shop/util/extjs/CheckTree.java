/*
 *  Copyright 2011, iSoftStone Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF iSoftStone CO., LTD.
 *  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED
 *  TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF NEWTOUCH CO., LTD.
 */
package com.work.shop.util.extjs;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lhj
 * @since 1.0
 * @version 1.0
 */
public class CheckTree {
	private String id;
	private String text;
	private String cls="";
	private boolean leaf;
	private boolean  checked;
	private List<CheckTree> children = new ArrayList<CheckTree>(0);
	
	public CheckTree(){
		
	}
	
	
	/**
	 * @param id
	 * @param text
	 * @param cls
	 * @param leaf
	 * @param checked
	 * @param children
	 */
	public CheckTree(String id, String text, String cls, boolean leaf,
			boolean checked, List<CheckTree> children) {
		super();
		this.id = id;
		this.text = text;
		this.cls = cls;
		this.leaf = leaf;
		this.checked = checked;
		this.children = children;
	}
	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the cls
	 */
	public String getCls() {
		return cls;
	}
	/**
	 * @param cls the cls to set
	 */
	public void setCls(String cls) {
		this.cls = cls;
	}
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the children
	 */
	public List<CheckTree> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<CheckTree> children) {
		this.children = children;
	}
	
}