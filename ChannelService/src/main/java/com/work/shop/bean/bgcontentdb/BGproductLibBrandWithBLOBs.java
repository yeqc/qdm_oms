package com.work.shop.bean.bgcontentdb;

public class BGproductLibBrandWithBLOBs extends BGproductLibBrand {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_lib_brand.brand_desc
     *
     * @mbggenerated
     */
    private String brandDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_lib_brand.description
     *
     * @mbggenerated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_lib_brand.brand_desc
     *
     * @return the value of product_lib_brand.brand_desc
     *
     * @mbggenerated
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_lib_brand.brand_desc
     *
     * @param brandDesc the value for product_lib_brand.brand_desc
     *
     * @mbggenerated
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc == null ? null : brandDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_lib_brand.description
     *
     * @return the value of product_lib_brand.description
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_lib_brand.description
     *
     * @param description the value for product_lib_brand.description
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}