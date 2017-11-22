/*
 *  Copyright 2011, iSoftStone Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF iSoftStone CO., LTD.
 *  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED
 *  TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF NEWTOUCH CO., LTD.
 */
package com.work.shop.util.extjs;

/**
 * 排序信息对象sortInfo。
 * @author lhj
 * @since 1.0
 * @version 1.0
 */
public class Sort {

	private String sort;//排序字段field
	private String dir;//升降序direction
	
	/**
	 * @param dir
	 * @param sort
	 */
	public Sort(String sort, String dir) {
		super();
		this.dir = dir;
		this.sort = sort;
	}

	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}
