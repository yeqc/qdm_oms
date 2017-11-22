package com.work.shop.bean;

import java.util.Date;

import com.work.shop.util.TimeUtil;

public class PromotionsInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.prom_code
     *
     * @mbggenerated
     */
    private String promCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.prom_title
     *
     * @mbggenerated
     */
    private String promTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.prom_status
     *
     * @mbggenerated
     */
    private Byte promStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.shop_code
     *
     * @mbggenerated
     */
    private String shopCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.shop_title
     *
     * @mbggenerated
     */
    private String shopTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.prom_type
     *
     * @mbggenerated
     */
    private Byte promType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.begin_time
     *
     * @mbggenerated
     */
    private Date beginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.backup
     *
     * @mbggenerated
     */
    private String backup;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_info.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.Id
     *
     * @return the value of promotions_info.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.Id
     *
     * @param id the value for promotions_info.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.prom_code
     *
     * @return the value of promotions_info.prom_code
     *
     * @mbggenerated
     */
    public String getPromCode() {
        return promCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.prom_code
     *
     * @param promCode the value for promotions_info.prom_code
     *
     * @mbggenerated
     */
    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.prom_title
     *
     * @return the value of promotions_info.prom_title
     *
     * @mbggenerated
     */
    public String getPromTitle() {
        return promTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.prom_title
     *
     * @param promTitle the value for promotions_info.prom_title
     *
     * @mbggenerated
     */
    public void setPromTitle(String promTitle) {
        this.promTitle = promTitle == null ? null : promTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.prom_status
     *
     * @return the value of promotions_info.prom_status
     *
     * @mbggenerated
     */
    public Byte getPromStatus() {
        return promStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.prom_status
     *
     * @param promStatus the value for promotions_info.prom_status
     *
     * @mbggenerated
     */
    public void setPromStatus(Byte promStatus) {
        this.promStatus = promStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.shop_code
     *
     * @return the value of promotions_info.shop_code
     *
     * @mbggenerated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.shop_code
     *
     * @param shopCode the value for promotions_info.shop_code
     *
     * @mbggenerated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.shop_title
     *
     * @return the value of promotions_info.shop_title
     *
     * @mbggenerated
     */
    public String getShopTitle() {
        return shopTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.shop_title
     *
     * @param shopTitle the value for promotions_info.shop_title
     *
     * @mbggenerated
     */
    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle == null ? null : shopTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.prom_type
     *
     * @return the value of promotions_info.prom_type
     *
     * @mbggenerated
     */
    public Byte getPromType() {
        return promType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.prom_type
     *
     * @param promType the value for promotions_info.prom_type
     *
     * @mbggenerated
     */
    public void setPromType(Byte promType) {
        this.promType = promType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.begin_time
     *
     * @return the value of promotions_info.begin_time
     *
     * @mbggenerated
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.begin_time
     *
     * @param beginTime the value for promotions_info.begin_time
     *
     * @mbggenerated
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.end_time
     *
     * @return the value of promotions_info.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.end_time
     *
     * @param endTime the value for promotions_info.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.backup
     *
     * @return the value of promotions_info.backup
     *
     * @mbggenerated
     */
    public String getBackup() {
        return backup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.backup
     *
     * @param backup the value for promotions_info.backup
     *
     * @mbggenerated
     */
    public void setBackup(String backup) {
        this.backup = backup == null ? null : backup.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_info.add_time
     *
     * @return the value of promotions_info.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_info.add_time
     *
     * @param addTime the value for promotions_info.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	@Override
	public String toString() {
		return "{活动编号：" + this.promCode + ", 活动名称：" + this.promTitle + ", 活动开始时间：" + TimeUtil.format(this.beginTime,TimeUtil.YYYY_MM_DD_HH_MM_SS)  + ", 活动结束时间：" + TimeUtil.format(this.endTime,TimeUtil.YYYY_MM_DD_HH_MM_SS) + ", 活动状态：" + (this.promStatus == 0 ? "未启用" : "启用") + "}";
	}
    
    
}