package com.work.shop.bean.bgcontentdb;

public class BGchannelGoodsExtensionWithBLOBs extends BGchannelGoodsExtension {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_extension.goods_desc
     *
     * @mbggenerated
     */
    private String goodsDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_goods_extension.phone_goods_desc
     *
     * @mbggenerated
     */
    private String phoneGoodsDesc;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_extension.goods_desc
     *
     * @return the value of channel_goods_extension.goods_desc
     *
     * @mbggenerated
     */
    public String getGoodsDesc() {
        return goodsDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_extension.goods_desc
     *
     * @param goodsDesc the value for channel_goods_extension.goods_desc
     *
     * @mbggenerated
     */
    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_goods_extension.phone_goods_desc
     *
     * @return the value of channel_goods_extension.phone_goods_desc
     *
     * @mbggenerated
     */
    public String getPhoneGoodsDesc() {
        return phoneGoodsDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_goods_extension.phone_goods_desc
     *
     * @param phoneGoodsDesc the value for channel_goods_extension.phone_goods_desc
     *
     * @mbggenerated
     */
    public void setPhoneGoodsDesc(String phoneGoodsDesc) {
        this.phoneGoodsDesc = phoneGoodsDesc == null ? null : phoneGoodsDesc.trim();
    }
}