package com.work.shop.vo;

import java.io.Serializable;
import java.util.List;

import com.work.shop.bean.TicketInfo;

public class QueueManagerVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9205264909210852840L;

	private String channelCode; // 渠道编码

	private String shopCode;// 店铺编号

	private String operUser;// 操作用户

	private String templateCode; // 模板编号

	private List<String> ticketList; // 调整单列表

	private int operateType = 0; // 操作类型 0 ：调整单操作 1 ：详情生成

	private String postageId; //运费模板Id
	
	private TicketInfo ticketInfo; //调整单信息
	
	private Byte ticketType;
	
	private int ticketIndex;
	
	private int ticketSize;
	
	private int ticketId; // 调整单Id

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public List<String> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<String> ticketList) {
		this.ticketList = ticketList;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getPostageId() {
		return postageId;
	}

	public void setPostageId(String postageId) {
		this.postageId = postageId;
	}

	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(TicketInfo ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public Byte getTicketType() {
		return ticketType;
	}

	public void setTicketType(Byte ticketType) {
		this.ticketType = ticketType;
	}

	public int getTicketIndex() {
		return ticketIndex;
	}

	public void setTicketIndex(int ticketIndex) {
		this.ticketIndex = ticketIndex;
	}

	public int getTicketSize() {
		return ticketSize;
	}

	public void setTicketSize(int ticketSize) {
		this.ticketSize = ticketSize;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
}
