package com.work.shop.vo;

import java.util.Date;

import com.work.shop.util.TimeUtil;

//店铺商品上下架调整单
public class ChannelGoodsTicketVo {
	
	private int id;
	private String ticketCode;//调整单编号
	private String channelCode; //渠道编码
	private String shopTitle;//经营店铺名称
	private String shopCode;//店铺编号
	private Date addTime;//单据添加时间
	private String ticketStatus;//单据状态
	private String operUser;//操作用户
	private Byte ticketType = -1;//单据类型,默认为-1,条件会排除-1
	private String isTiming; //是否定时执行
	private Date excetTime;//执行时间
	private String excetTimes;//执行时间
	private String note; //执行结果
	private String userName; //用户名
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getExcetTime() {
		return excetTime;
	}
	public void setExcetTime(Date excetTime) {
		this.excetTime = excetTime;
	}
	public String getIsTiming() {
		return isTiming;
	}
	public void setIsTiming(String isTiming) {
		this.isTiming = isTiming;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTicketCode() {
		return ticketCode;
	}
	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShopTitle() {
		return shopTitle;
	}
	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public String getOperUser() {
		return operUser;
	}
	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	public Byte getTicketType() {
		return ticketType;
	}
	public void setTicketType(Byte ticketType) {
		this.ticketType = ticketType;
	}

	public String getFormatAddTime() {
		String time = "";
		if (addTime != null) {
			time = TimeUtil.formatDate(addTime);
		}
		return time;
	}

	public String getFormatExcetTime() {
		String time = "";
		if (excetTime != null) {
			time = TimeUtil.formatDate(excetTime);
		}
		return time;
	}

	public String getExcetTimes() {
		return excetTimes;
	}
	public void setExcetTimes(String excetTimes) {
		this.excetTimes = excetTimes;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
