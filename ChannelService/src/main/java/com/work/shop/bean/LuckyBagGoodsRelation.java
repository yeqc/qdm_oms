package com.work.shop.bean;

public class LuckyBagGoodsRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_goods_relation.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_goods_relation.lucky_bag_sku
     *
     * @mbggenerated
     */
    private String luckyBagSku;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_goods_relation.subset_code
     *
     * @mbggenerated
     */
    private String subsetCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_goods_relation.sku
     *
     * @mbggenerated
     */
    private String sku;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_goods_relation.id
     *
     * @return the value of lucky_bag_goods_relation.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_goods_relation.id
     *
     * @param id the value for lucky_bag_goods_relation.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_goods_relation.lucky_bag_sku
     *
     * @return the value of lucky_bag_goods_relation.lucky_bag_sku
     *
     * @mbggenerated
     */
    public String getLuckyBagSku() {
        return luckyBagSku;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_goods_relation.lucky_bag_sku
     *
     * @param luckyBagSku the value for lucky_bag_goods_relation.lucky_bag_sku
     *
     * @mbggenerated
     */
    public void setLuckyBagSku(String luckyBagSku) {
        this.luckyBagSku = luckyBagSku == null ? null : luckyBagSku.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_goods_relation.subset_code
     *
     * @return the value of lucky_bag_goods_relation.subset_code
     *
     * @mbggenerated
     */
    public String getSubsetCode() {
        return subsetCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_goods_relation.subset_code
     *
     * @param subsetCode the value for lucky_bag_goods_relation.subset_code
     *
     * @mbggenerated
     */
    public void setSubsetCode(String subsetCode) {
        this.subsetCode = subsetCode == null ? null : subsetCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_goods_relation.sku
     *
     * @return the value of lucky_bag_goods_relation.sku
     *
     * @mbggenerated
     */
    public String getSku() {
        return sku;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_goods_relation.sku
     *
     * @param sku the value for lucky_bag_goods_relation.sku
     *
     * @mbggenerated
     */
    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }
}