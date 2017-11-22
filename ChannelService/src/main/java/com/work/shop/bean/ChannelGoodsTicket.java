package com.work.shop.bean;

import java.util.Date;

public class ChannelGoodsTicket {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.ticket_code
     *
     * @mbggenerated
     */
    private String ticketCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.channel_code
     *
     * @mbggenerated
     */
    private String channelCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.shop_code
     *
     * @mbggenerated
     */
    private String shopCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.product_goods_sn
     *
     * @mbggenerated
     */
    private String productGoodsSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.channel_goods_code
     *
     * @mbggenerated
     */
    private String channelGoodsCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.ticket_status
     *
     * @mbggenerated
     */
    private String ticketStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.oper_user
     *
     * @mbggenerated
     */
    private String operUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.note
     *
     * @mbggenerated
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.is_timing
     *
     * @mbggenerated
     */
    private Byte isTiming;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.exec_time
     *
     * @mbggenerated
     */
    private Date execTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.is_sync
     *
     * @mbggenerated
     */
    private Byte isSync;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_ticket.ticket_type
     *
     * @mbggenerated
     */
    private Byte ticketType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.Id
     *
     * @return the value of channel_goods_ticket.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.Id
     *
     * @param id the value for channel_goods_ticket.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.ticket_code
     *
     * @return the value of channel_goods_ticket.ticket_code
     *
     * @mbggenerated
     */
    public String getTicketCode() {
        return ticketCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.ticket_code
     *
     * @param ticketCode the value for channel_goods_ticket.ticket_code
     *
     * @mbggenerated
     */
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode == null ? null : ticketCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.channel_code
     *
     * @return the value of channel_goods_ticket.channel_code
     *
     * @mbggenerated
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.channel_code
     *
     * @param channelCode the value for channel_goods_ticket.channel_code
     *
     * @mbggenerated
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.shop_code
     *
     * @return the value of channel_goods_ticket.shop_code
     *
     * @mbggenerated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.shop_code
     *
     * @param shopCode the value for channel_goods_ticket.shop_code
     *
     * @mbggenerated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.product_goods_sn
     *
     * @return the value of channel_goods_ticket.product_goods_sn
     *
     * @mbggenerated
     */
    public String getProductGoodsSn() {
        return productGoodsSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.product_goods_sn
     *
     * @param productGoodsSn the value for channel_goods_ticket.product_goods_sn
     *
     * @mbggenerated
     */
    public void setProductGoodsSn(String productGoodsSn) {
        this.productGoodsSn = productGoodsSn == null ? null : productGoodsSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.channel_goods_code
     *
     * @return the value of channel_goods_ticket.channel_goods_code
     *
     * @mbggenerated
     */
    public String getChannelGoodsCode() {
        return channelGoodsCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.channel_goods_code
     *
     * @param channelGoodsCode the value for channel_goods_ticket.channel_goods_code
     *
     * @mbggenerated
     */
    public void setChannelGoodsCode(String channelGoodsCode) {
        this.channelGoodsCode = channelGoodsCode == null ? null : channelGoodsCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.add_time
     *
     * @return the value of channel_goods_ticket.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.add_time
     *
     * @param addTime the value for channel_goods_ticket.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.ticket_status
     *
     * @return the value of channel_goods_ticket.ticket_status
     *
     * @mbggenerated
     */
    public String getTicketStatus() {
        return ticketStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.ticket_status
     *
     * @param ticketStatus the value for channel_goods_ticket.ticket_status
     *
     * @mbggenerated
     */
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus == null ? null : ticketStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.oper_user
     *
     * @return the value of channel_goods_ticket.oper_user
     *
     * @mbggenerated
     */
    public String getOperUser() {
        return operUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.oper_user
     *
     * @param operUser the value for channel_goods_ticket.oper_user
     *
     * @mbggenerated
     */
    public void setOperUser(String operUser) {
        this.operUser = operUser == null ? null : operUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.note
     *
     * @return the value of channel_goods_ticket.note
     *
     * @mbggenerated
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.note
     *
     * @param note the value for channel_goods_ticket.note
     *
     * @mbggenerated
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.is_timing
     *
     * @return the value of channel_goods_ticket.is_timing
     *
     * @mbggenerated
     */
    public Byte getIsTiming() {
        return isTiming;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.is_timing
     *
     * @param isTiming the value for channel_goods_ticket.is_timing
     *
     * @mbggenerated
     */
    public void setIsTiming(Byte isTiming) {
        this.isTiming = isTiming;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.exec_time
     *
     * @return the value of channel_goods_ticket.exec_time
     *
     * @mbggenerated
     */
    public Date getExecTime() {
        return execTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.exec_time
     *
     * @param execTime the value for channel_goods_ticket.exec_time
     *
     * @mbggenerated
     */
    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.is_sync
     *
     * @return the value of channel_goods_ticket.is_sync
     *
     * @mbggenerated
     */
    public Byte getIsSync() {
        return isSync;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.is_sync
     *
     * @param isSync the value for channel_goods_ticket.is_sync
     *
     * @mbggenerated
     */
    public void setIsSync(Byte isSync) {
        this.isSync = isSync;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_ticket.ticket_type
     *
     * @return the value of channel_goods_ticket.ticket_type
     *
     * @mbggenerated
     */
    public Byte getTicketType() {
        return ticketType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_ticket.ticket_type
     *
     * @param ticketType the value for channel_goods_ticket.ticket_type
     *
     * @mbggenerated
     */
    public void setTicketType(Byte ticketType) {
        this.ticketType = ticketType;
    }

	@Override
	public String toString() {
		return "ChannelGoodsTicket [id=" + id + ", ticketCode=" + ticketCode + ", channelCode=" + channelCode + ", shopCode=" + shopCode + ", productGoodsSn=" + productGoodsSn + ", channelGoodsCode="
				+ channelGoodsCode + ", addTime=" + addTime + ", ticketStatus=" + ticketStatus + ", operUser=" + operUser + ", note=" + note + ", isTiming=" + isTiming + ", execTime=" + execTime
				+ ", isSync=" + isSync + ", ticketType=" + ticketType + "]";
	}

}