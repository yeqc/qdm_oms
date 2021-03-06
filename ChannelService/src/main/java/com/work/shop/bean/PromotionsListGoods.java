package com.work.shop.bean;

public class PromotionsListGoods {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_goods.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_goods.prom_code
     *
     * @mbggenerated
     */
    private String promCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promotions_list_goods.goods_sn
     *
     * @mbggenerated
     */
    private String goodsSn;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_goods.Id
     *
     * @return the value of promotions_list_goods.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_goods.Id
     *
     * @param id the value for promotions_list_goods.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_goods.prom_code
     *
     * @return the value of promotions_list_goods.prom_code
     *
     * @mbggenerated
     */
    public String getPromCode() {
        return promCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_goods.prom_code
     *
     * @param promCode the value for promotions_list_goods.prom_code
     *
     * @mbggenerated
     */
    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promotions_list_goods.goods_sn
     *
     * @return the value of promotions_list_goods.goods_sn
     *
     * @mbggenerated
     */
    public String getGoodsSn() {
        return goodsSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promotions_list_goods.goods_sn
     *
     * @param goodsSn the value for promotions_list_goods.goods_sn
     *
     * @mbggenerated
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn == null ? null : goodsSn.trim();
    }

	@Override
	public String toString() {
		return "{需够商品编码=" + goodsSn + "}";
	}

	@Override
	public boolean equals(Object obj) {
		PromotionsListGoods bean = (PromotionsListGoods)obj;
		return this.goodsSn.equals(bean.getGoodsSn());
	}
	
	
}