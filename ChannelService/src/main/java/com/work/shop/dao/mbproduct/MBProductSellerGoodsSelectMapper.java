package com.work.shop.dao.mbproduct;

import java.util.List;
import java.util.Map;

import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsSelect;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsSelectExample;
import com.work.shop.invocation.ReadOnly;

public interface MBProductSellerGoodsSelectMapper {


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MBProductSellerGoodsSelectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MBProductSellerGoodsSelect> selectByExampleWithBLOBs(MBProductSellerGoodsSelectExample example);

    @ReadOnly
    List<ChannelApiGoods> selectChannelItem(Map<String, Object> param);
    @ReadOnly
    int selectChannelItemCount(Map<String, Object> param);

}