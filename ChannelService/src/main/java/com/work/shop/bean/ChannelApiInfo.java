package com.work.shop.bean;

public class ChannelApiInfo implements Comparable<ChannelApiInfo> {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.channel_code
     *
     * @mbggenerated
     */
    private String channelCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.channel_title
     *
     * @mbggenerated
     */
    private String channelTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.api_type
     *
     * @mbggenerated
     */
    private String apiType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.api_property
     *
     * @mbggenerated
     */
    private String apiProperty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.api_property_type
     *
     * @mbggenerated
     */
    private String apiPropertyType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.is_need
     *
     * @mbggenerated
     */
    private Integer isNeed;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.channel_goods_property
     *
     * @mbggenerated
     */
    private String channelGoodsProperty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_api_info.api_property_desc
     *
     * @mbggenerated
     */
    private String apiPropertyDesc;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.id
     *
     * @return the value of channel_api_info.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.id
     *
     * @param id the value for channel_api_info.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.channel_code
     *
     * @return the value of channel_api_info.channel_code
     *
     * @mbggenerated
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.channel_code
     *
     * @param channelCode the value for channel_api_info.channel_code
     *
     * @mbggenerated
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.channel_title
     *
     * @return the value of channel_api_info.channel_title
     *
     * @mbggenerated
     */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.channel_title
     *
     * @param channelTitle the value for channel_api_info.channel_title
     *
     * @mbggenerated
     */
    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle == null ? null : channelTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.api_type
     *
     * @return the value of channel_api_info.api_type
     *
     * @mbggenerated
     */
    public String getApiType() {
        return apiType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.api_type
     *
     * @param apiType the value for channel_api_info.api_type
     *
     * @mbggenerated
     */
    public void setApiType(String apiType) {
        this.apiType = apiType == null ? null : apiType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.api_property
     *
     * @return the value of channel_api_info.api_property
     *
     * @mbggenerated
     */
    public String getApiProperty() {
        return apiProperty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.api_property
     *
     * @param apiProperty the value for channel_api_info.api_property
     *
     * @mbggenerated
     */
    public void setApiProperty(String apiProperty) {
        this.apiProperty = apiProperty == null ? null : apiProperty.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.api_property_type
     *
     * @return the value of channel_api_info.api_property_type
     *
     * @mbggenerated
     */
    public String getApiPropertyType() {
        return apiPropertyType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.api_property_type
     *
     * @param apiPropertyType the value for channel_api_info.api_property_type
     *
     * @mbggenerated
     */
    public void setApiPropertyType(String apiPropertyType) {
        this.apiPropertyType = apiPropertyType == null ? null : apiPropertyType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.is_need
     *
     * @return the value of channel_api_info.is_need
     *
     * @mbggenerated
     */
    public Integer getIsNeed() {
        return isNeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.is_need
     *
     * @param isNeed the value for channel_api_info.is_need
     *
     * @mbggenerated
     */
    public void setIsNeed(Integer isNeed) {
        this.isNeed = isNeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.channel_goods_property
     *
     * @return the value of channel_api_info.channel_goods_property
     *
     * @mbggenerated
     */
    public String getChannelGoodsProperty() {
        return channelGoodsProperty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.channel_goods_property
     *
     * @param channelGoodsProperty the value for channel_api_info.channel_goods_property
     *
     * @mbggenerated
     */
    public void setChannelGoodsProperty(String channelGoodsProperty) {
        this.channelGoodsProperty = channelGoodsProperty == null ? null : channelGoodsProperty.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_api_info.api_property_desc
     *
     * @return the value of channel_api_info.api_property_desc
     *
     * @mbggenerated
     */
    public String getApiPropertyDesc() {
        return apiPropertyDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_api_info.api_property_desc
     *
     * @param apiPropertyDesc the value for channel_api_info.api_property_desc
     *
     * @mbggenerated
     */
    public void setApiPropertyDesc(String apiPropertyDesc) {
        this.apiPropertyDesc = apiPropertyDesc == null ? null : apiPropertyDesc.trim();
    }
    
    public int compareTo(ChannelApiInfo obj) {  
        // TODO Auto-generated method stub  
        if (null == obj) return 1;  
        else {  
            return this.apiProperty.compareTo(obj.apiProperty);  
        }  
          
    }  
}