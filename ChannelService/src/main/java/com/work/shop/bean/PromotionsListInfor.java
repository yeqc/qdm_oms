package com.work.shop.bean;

import java.math.BigDecimal;

public class PromotionsListInfor {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_infor.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_infor.prom_code
     *
     * @mbggenerated
     */
    private String promCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_infor.goods_count
     *
     * @mbggenerated
     */
    private Integer goodsCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_infor.limit_money
     *
     * @mbggenerated
     */
    private BigDecimal limitMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_infor.gifts_count
     *
     * @mbggenerated
     */
    private Integer giftsCount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_infor.Id
     *
     * @return the value of promotions_list_infor.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_infor.Id
     *
     * @param id the value for promotions_list_infor.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_infor.prom_code
     *
     * @return the value of promotions_list_infor.prom_code
     *
     * @mbggenerated
     */
    public String getPromCode() {
        return promCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_infor.prom_code
     *
     * @param promCode the value for promotions_list_infor.prom_code
     *
     * @mbggenerated
     */
    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_infor.goods_count
     *
     * @return the value of promotions_list_infor.goods_count
     *
     * @mbggenerated
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_infor.goods_count
     *
     * @param goodsCount the value for promotions_list_infor.goods_count
     *
     * @mbggenerated
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_infor.limit_money
     *
     * @return the value of promotions_list_infor.limit_money
     *
     * @mbggenerated
     */
    public BigDecimal getLimitMoney() {
        return limitMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_infor.limit_money
     *
     * @param limitMoney the value for promotions_list_infor.limit_money
     *
     * @mbggenerated
     */
    public void setLimitMoney(BigDecimal limitMoney) {
        this.limitMoney = limitMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_infor.gifts_count
     *
     * @return the value of promotions_list_infor.gifts_count
     *
     * @mbggenerated
     */
    public Integer getGiftsCount() {
        return giftsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_infor.gifts_count
     *
     * @param giftsCount the value for promotions_list_infor.gifts_count
     *
     * @mbggenerated
     */
    public void setGiftsCount(Integer giftsCount) {
        this.giftsCount = giftsCount;
    }

	@Override
	public String toString() {
		return "{需够商品数量=" + goodsCount + ", 需购商品金额=" + limitMoney + ", 赠品数量=" + giftsCount + "}";
	}
}