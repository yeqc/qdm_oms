package com.work.shop.bean;

import java.util.Date;

public class ChannelTemplateContent {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.template_code
     *
     * @mbggenerated
     */
    private String templateCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.module_type
     *
     * @mbggenerated
     */
    private Byte moduleType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.order_num
     *
     * @mbggenerated
     */
    private Byte orderNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.module_id
     *
     * @mbggenerated
     */
    private Integer moduleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template_content.module_content
     *
     * @mbggenerated
     */
    private String moduleContent;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.Id
     *
     * @return the value of channel_template_content.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.Id
     *
     * @param id the value for channel_template_content.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.template_code
     *
     * @return the value of channel_template_content.template_code
     *
     * @mbggenerated
     */
    public String getTemplateCode() {
        return templateCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.template_code
     *
     * @param templateCode the value for channel_template_content.template_code
     *
     * @mbggenerated
     */
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.module_type
     *
     * @return the value of channel_template_content.module_type
     *
     * @mbggenerated
     */
    public Byte getModuleType() {
        return moduleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.module_type
     *
     * @param moduleType the value for channel_template_content.module_type
     *
     * @mbggenerated
     */
    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.order_num
     *
     * @return the value of channel_template_content.order_num
     *
     * @mbggenerated
     */
    public Byte getOrderNum() {
        return orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.order_num
     *
     * @param orderNum the value for channel_template_content.order_num
     *
     * @mbggenerated
     */
    public void setOrderNum(Byte orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.module_id
     *
     * @return the value of channel_template_content.module_id
     *
     * @mbggenerated
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.module_id
     *
     * @param moduleId the value for channel_template_content.module_id
     *
     * @mbggenerated
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.add_time
     *
     * @return the value of channel_template_content.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.add_time
     *
     * @param addTime the value for channel_template_content.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.update_time
     *
     * @return the value of channel_template_content.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.update_time
     *
     * @param updateTime the value for channel_template_content.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template_content.module_content
     *
     * @return the value of channel_template_content.module_content
     *
     * @mbggenerated
     */
    public String getModuleContent() {
        return moduleContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template_content.module_content
     *
     * @param moduleContent the value for channel_template_content.module_content
     *
     * @mbggenerated
     */
    public void setModuleContent(String moduleContent) {
        this.moduleContent = moduleContent == null ? null : moduleContent.trim();
    }
}