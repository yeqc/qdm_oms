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
 * extjs的menu
 * @author lhj
 * @since 1.0
 * @version 1.0
 */
public class ExtMenu {
	private String id;
	
	private String text;
	
	private String url;
	
	private String handler;
	
	private List<ExtMenu> menu = new ArrayList<ExtMenu>(0);

	
	/**
	 * @param id
	 * @param text
	 * @param url
	 * @param handler
	 * @param children
	 */
	public ExtMenu(String id, String text, String url, String handler,
			List<ExtMenu> menu) {
		super();
		this.id = id;
		this.text = text;
		this.url = url;
		this.handler = handler;
		this.menu = menu;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtMenu other = (ExtMenu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}



	/**
	 * @return the menu
	 */
	public List<ExtMenu> getMenu() {
		return menu;
	}



	/**
	 * @param menu the menu to set
	 */
	public void setMenu(List<ExtMenu> menu) {
		this.menu = menu;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}
	
}
