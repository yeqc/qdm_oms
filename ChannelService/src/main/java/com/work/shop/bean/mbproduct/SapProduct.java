package com.work.shop.bean.mbproduct;

import java.util.Date;

public class SapProduct {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.barcode_id
     *
     * @mbggenerated
     */
    private Long barcodeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.prod_cls_num
     *
     * @mbggenerated
     */
    private String prodClsNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.prod_num
     *
     * @mbggenerated
     */
    private String prodNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.color_code
     *
     * @mbggenerated
     */
    private String colorCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.color_name
     *
     * @mbggenerated
     */
    private String colorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.spec_code
     *
     * @mbggenerated
     */
    private String specCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.spec_name
     *
     * @mbggenerated
     */
    private String specName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.inner_bc
     *
     * @mbggenerated
     */
    private String innerBc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.intnl_bc
     *
     * @mbggenerated
     */
    private String intnlBc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.prod_status
     *
     * @mbggenerated
     */
    private String prodStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.prod_grid
     *
     * @mbggenerated
     */
    private String prodGrid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.is_samples
     *
     * @mbggenerated
     */
    private Byte isSamples;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.create_user
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.create_date
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.last_update_user
     *
     * @mbggenerated
     */
    private String lastUpdateUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.last_update_date
     *
     * @mbggenerated
     */
    private Date lastUpdateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sap_product.is_sap
     *
     * @mbggenerated
     */
    private Byte isSap;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.barcode_id
     *
     * @return the value of sap_product.barcode_id
     *
     * @mbggenerated
     */
    public Long getBarcodeId() {
        return barcodeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.barcode_id
     *
     * @param barcodeId the value for sap_product.barcode_id
     *
     * @mbggenerated
     */
    public void setBarcodeId(Long barcodeId) {
        this.barcodeId = barcodeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.prod_cls_num
     *
     * @return the value of sap_product.prod_cls_num
     *
     * @mbggenerated
     */
    public String getProdClsNum() {
        return prodClsNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.prod_cls_num
     *
     * @param prodClsNum the value for sap_product.prod_cls_num
     *
     * @mbggenerated
     */
    public void setProdClsNum(String prodClsNum) {
        this.prodClsNum = prodClsNum == null ? null : prodClsNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.prod_num
     *
     * @return the value of sap_product.prod_num
     *
     * @mbggenerated
     */
    public String getProdNum() {
        return prodNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.prod_num
     *
     * @param prodNum the value for sap_product.prod_num
     *
     * @mbggenerated
     */
    public void setProdNum(String prodNum) {
        this.prodNum = prodNum == null ? null : prodNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.color_code
     *
     * @return the value of sap_product.color_code
     *
     * @mbggenerated
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.color_code
     *
     * @param colorCode the value for sap_product.color_code
     *
     * @mbggenerated
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode == null ? null : colorCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.color_name
     *
     * @return the value of sap_product.color_name
     *
     * @mbggenerated
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.color_name
     *
     * @param colorName the value for sap_product.color_name
     *
     * @mbggenerated
     */
    public void setColorName(String colorName) {
        this.colorName = colorName == null ? null : colorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.spec_code
     *
     * @return the value of sap_product.spec_code
     *
     * @mbggenerated
     */
    public String getSpecCode() {
        return specCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.spec_code
     *
     * @param specCode the value for sap_product.spec_code
     *
     * @mbggenerated
     */
    public void setSpecCode(String specCode) {
        this.specCode = specCode == null ? null : specCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.spec_name
     *
     * @return the value of sap_product.spec_name
     *
     * @mbggenerated
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.spec_name
     *
     * @param specName the value for sap_product.spec_name
     *
     * @mbggenerated
     */
    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.inner_bc
     *
     * @return the value of sap_product.inner_bc
     *
     * @mbggenerated
     */
    public String getInnerBc() {
        return innerBc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.inner_bc
     *
     * @param innerBc the value for sap_product.inner_bc
     *
     * @mbggenerated
     */
    public void setInnerBc(String innerBc) {
        this.innerBc = innerBc == null ? null : innerBc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.intnl_bc
     *
     * @return the value of sap_product.intnl_bc
     *
     * @mbggenerated
     */
    public String getIntnlBc() {
        return intnlBc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.intnl_bc
     *
     * @param intnlBc the value for sap_product.intnl_bc
     *
     * @mbggenerated
     */
    public void setIntnlBc(String intnlBc) {
        this.intnlBc = intnlBc == null ? null : intnlBc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.prod_status
     *
     * @return the value of sap_product.prod_status
     *
     * @mbggenerated
     */
    public String getProdStatus() {
        return prodStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.prod_status
     *
     * @param prodStatus the value for sap_product.prod_status
     *
     * @mbggenerated
     */
    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus == null ? null : prodStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.prod_grid
     *
     * @return the value of sap_product.prod_grid
     *
     * @mbggenerated
     */
    public String getProdGrid() {
        return prodGrid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.prod_grid
     *
     * @param prodGrid the value for sap_product.prod_grid
     *
     * @mbggenerated
     */
    public void setProdGrid(String prodGrid) {
        this.prodGrid = prodGrid == null ? null : prodGrid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.is_samples
     *
     * @return the value of sap_product.is_samples
     *
     * @mbggenerated
     */
    public Byte getIsSamples() {
        return isSamples;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.is_samples
     *
     * @param isSamples the value for sap_product.is_samples
     *
     * @mbggenerated
     */
    public void setIsSamples(Byte isSamples) {
        this.isSamples = isSamples;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.create_user
     *
     * @return the value of sap_product.create_user
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.create_user
     *
     * @param createUser the value for sap_product.create_user
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.create_date
     *
     * @return the value of sap_product.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.create_date
     *
     * @param createDate the value for sap_product.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.last_update_user
     *
     * @return the value of sap_product.last_update_user
     *
     * @mbggenerated
     */
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.last_update_user
     *
     * @param lastUpdateUser the value for sap_product.last_update_user
     *
     * @mbggenerated
     */
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser == null ? null : lastUpdateUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.last_update_date
     *
     * @return the value of sap_product.last_update_date
     *
     * @mbggenerated
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.last_update_date
     *
     * @param lastUpdateDate the value for sap_product.last_update_date
     *
     * @mbggenerated
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sap_product.is_sap
     *
     * @return the value of sap_product.is_sap
     *
     * @mbggenerated
     */
    public Byte getIsSap() {
        return isSap;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sap_product.is_sap
     *
     * @param isSap the value for sap_product.is_sap
     *
     * @mbggenerated
     */
    public void setIsSap(Byte isSap) {
        this.isSap = isSap;
    }
}