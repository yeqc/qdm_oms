package com.work.shop.bean;

public class GiftsGoodsList {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gifts_goods_list.Id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gifts_goods_list.prom_code
     *
     * @mbggenerated
     */
    private String promCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gifts_goods_list.goods_sn
     *
     * @mbggenerated
     */
    private String goodsSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gifts_goods_list.gifts_sum
     *
     * @mbggenerated
     */
    private Integer giftsSum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gifts_goods_list.Id
     *
     * @return the value of gifts_goods_list.Id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gifts_goods_list.Id
     *
     * @param id the value for gifts_goods_list.Id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gifts_goods_list.prom_code
     *
     * @return the value of gifts_goods_list.prom_code
     *
     * @mbggenerated
     */
    public String getPromCode() {
        return promCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gifts_goods_list.prom_code
     *
     * @param promCode the value for gifts_goods_list.prom_code
     *
     * @mbggenerated
     */
    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gifts_goods_list.goods_sn
     *
     * @return the value of gifts_goods_list.goods_sn
     *
     * @mbggenerated
     */
    public String getGoodsSn() {
        return goodsSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gifts_goods_list.goods_sn
     *
     * @param goodsSn the value for gifts_goods_list.goods_sn
     *
     * @mbggenerated
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn == null ? null : goodsSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gifts_goods_list.gifts_sum
     *
     * @return the value of gifts_goods_list.gifts_sum
     *
     * @mbggenerated
     */
    public Integer getGiftsSum() {
        return giftsSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gifts_goods_list.gifts_sum
     *
     * @param giftsSum the value for gifts_goods_list.gifts_sum
     *
     * @mbggenerated
     */
    public void setGiftsSum(Integer giftsSum) {
        this.giftsSum = giftsSum;
    }

	@Override
	public String toString() {
		return "{赠品编码=" + goodsSn + ", 赠品初始数量=" + giftsSum + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goodsSn == null) ? 0 : goodsSn.hashCode());
		result = prime * result
				+ ((promCode == null) ? 0 : promCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GiftsGoodsList other = (GiftsGoodsList) obj;
		if (goodsSn == null) {
			if (other.goodsSn != null)
				return false;
		} else if (!goodsSn.equals(other.goodsSn))
			return false;
		if (promCode == null) {
			if (other.promCode != null)
				return false;
		} else if (!promCode.equals(other.promCode))
			return false;
		return true;
	}
}