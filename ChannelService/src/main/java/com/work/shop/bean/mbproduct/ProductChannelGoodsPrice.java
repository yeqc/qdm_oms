package com.work.shop.bean.mbproduct;

import java.math.BigDecimal;
import java.util.Date;

public class ProductChannelGoodsPrice {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.seller_code
     *
     * @mbggenerated
     */
    private String sellerCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.channel_code
     *
     * @mbggenerated
     */
    private String channelCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.product_sys_code
     *
     * @mbggenerated
     */
    private String productSysCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.barcode_sys_code
     *
     * @mbggenerated
     */
    private String barcodeSysCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.sale_attr1_value_code
     *
     * @mbggenerated
     */
    private String saleAttr1ValueCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.sale_attr2_value_code
     *
     * @mbggenerated
     */
    private String saleAttr2ValueCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.spec_price
     *
     * @mbggenerated
     */
    private BigDecimal specPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.status
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.create_user
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.create_date
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.last_update_user
     *
     * @mbggenerated
     */
    private String lastUpdateUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.last_update_date
     *
     * @mbggenerated
     */
    private Date lastUpdateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_channel_goods_price.last_controller_name
     *
     * @mbggenerated
     */
    private String lastControllerName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.id
     *
     * @return the value of product_channel_goods_price.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.id
     *
     * @param id the value for product_channel_goods_price.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.seller_code
     *
     * @return the value of product_channel_goods_price.seller_code
     *
     * @mbggenerated
     */
    public String getSellerCode() {
        return sellerCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.seller_code
     *
     * @param sellerCode the value for product_channel_goods_price.seller_code
     *
     * @mbggenerated
     */
    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode == null ? null : sellerCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.channel_code
     *
     * @return the value of product_channel_goods_price.channel_code
     *
     * @mbggenerated
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.channel_code
     *
     * @param channelCode the value for product_channel_goods_price.channel_code
     *
     * @mbggenerated
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.product_sys_code
     *
     * @return the value of product_channel_goods_price.product_sys_code
     *
     * @mbggenerated
     */
    public String getProductSysCode() {
        return productSysCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.product_sys_code
     *
     * @param productSysCode the value for product_channel_goods_price.product_sys_code
     *
     * @mbggenerated
     */
    public void setProductSysCode(String productSysCode) {
        this.productSysCode = productSysCode == null ? null : productSysCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.barcode_sys_code
     *
     * @return the value of product_channel_goods_price.barcode_sys_code
     *
     * @mbggenerated
     */
    public String getBarcodeSysCode() {
        return barcodeSysCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.barcode_sys_code
     *
     * @param barcodeSysCode the value for product_channel_goods_price.barcode_sys_code
     *
     * @mbggenerated
     */
    public void setBarcodeSysCode(String barcodeSysCode) {
        this.barcodeSysCode = barcodeSysCode == null ? null : barcodeSysCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.sale_attr1_value_code
     *
     * @return the value of product_channel_goods_price.sale_attr1_value_code
     *
     * @mbggenerated
     */
    public String getSaleAttr1ValueCode() {
        return saleAttr1ValueCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.sale_attr1_value_code
     *
     * @param saleAttr1ValueCode the value for product_channel_goods_price.sale_attr1_value_code
     *
     * @mbggenerated
     */
    public void setSaleAttr1ValueCode(String saleAttr1ValueCode) {
        this.saleAttr1ValueCode = saleAttr1ValueCode == null ? null : saleAttr1ValueCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.sale_attr2_value_code
     *
     * @return the value of product_channel_goods_price.sale_attr2_value_code
     *
     * @mbggenerated
     */
    public String getSaleAttr2ValueCode() {
        return saleAttr2ValueCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.sale_attr2_value_code
     *
     * @param saleAttr2ValueCode the value for product_channel_goods_price.sale_attr2_value_code
     *
     * @mbggenerated
     */
    public void setSaleAttr2ValueCode(String saleAttr2ValueCode) {
        this.saleAttr2ValueCode = saleAttr2ValueCode == null ? null : saleAttr2ValueCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.spec_price
     *
     * @return the value of product_channel_goods_price.spec_price
     *
     * @mbggenerated
     */
    public BigDecimal getSpecPrice() {
        return specPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.spec_price
     *
     * @param specPrice the value for product_channel_goods_price.spec_price
     *
     * @mbggenerated
     */
    public void setSpecPrice(BigDecimal specPrice) {
        this.specPrice = specPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.status
     *
     * @return the value of product_channel_goods_price.status
     *
     * @mbggenerated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.status
     *
     * @param status the value for product_channel_goods_price.status
     *
     * @mbggenerated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.create_user
     *
     * @return the value of product_channel_goods_price.create_user
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.create_user
     *
     * @param createUser the value for product_channel_goods_price.create_user
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.create_date
     *
     * @return the value of product_channel_goods_price.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.create_date
     *
     * @param createDate the value for product_channel_goods_price.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.last_update_user
     *
     * @return the value of product_channel_goods_price.last_update_user
     *
     * @mbggenerated
     */
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.last_update_user
     *
     * @param lastUpdateUser the value for product_channel_goods_price.last_update_user
     *
     * @mbggenerated
     */
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser == null ? null : lastUpdateUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.last_update_date
     *
     * @return the value of product_channel_goods_price.last_update_date
     *
     * @mbggenerated
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.last_update_date
     *
     * @param lastUpdateDate the value for product_channel_goods_price.last_update_date
     *
     * @mbggenerated
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_channel_goods_price.last_controller_name
     *
     * @return the value of product_channel_goods_price.last_controller_name
     *
     * @mbggenerated
     */
    public String getLastControllerName() {
        return lastControllerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_channel_goods_price.last_controller_name
     *
     * @param lastControllerName the value for product_channel_goods_price.last_controller_name
     *
     * @mbggenerated
     */
    public void setLastControllerName(String lastControllerName) {
        this.lastControllerName = lastControllerName == null ? null : lastControllerName.trim();
    }
}