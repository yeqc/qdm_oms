/*
 *  Copyright 2012, iSoftStone Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF iSoftStone CO., LTD.
 *  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED
 *  TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF NEWTOUCH CO., LTD.
 */
package com.work.shop.vo;


/**
 * 返回一个对象时,同时有返回状态时用。把对象放在这个类中。
 * 状态放在父类ResultVO中。如登录时，会返回登录的状态，成功，失败，用户不存在。
 * @author lhj
 *
 */
public class ObjectResultVO<T> extends ResultVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**返回一个对象*/
	private T yourObject;
   

	/**
	 * @return the yourObject
	 */
	public T getYourObject() {
		return yourObject;
	}

	/**
	 * 
	 */
	public ObjectResultVO() {
		super();
	}

	/**
	 * @param code
	 * @param message
	 */
	public ObjectResultVO(String code) {
		super(code);
	}

	/**
	 * @param code
	 * @param message
	 */
	public ObjectResultVO(String code, String message) {
		super(code, message);
	}
	
	/**
	 * @param yourObject
	 */
	public ObjectResultVO(T yourObject) {
		super();
		this.yourObject = yourObject;
	}
	
	/**
	 * @param code
	 * @param yourObject
	 */
	public ObjectResultVO(String code,  T yourObject) {
		super(code);
		this.yourObject = yourObject;
	}
	
	/**
	 * @param code
	 * @param message
	 * @param yourObject
	 */
	public ObjectResultVO(String code, String message, T yourObject) {
		super(code, message);
		this.yourObject = yourObject;
	}


	/**
	 * @param yourObject the yourObject to set
	 */
	public void setYourObject(T yourObject) {
		this.yourObject = yourObject;
	}
	
}
