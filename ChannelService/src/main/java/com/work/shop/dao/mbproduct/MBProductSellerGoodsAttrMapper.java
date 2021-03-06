package com.work.shop.dao.mbproduct;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttr;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MBProductSellerGoodsAttrMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MBProductSellerGoodsAttrExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MBProductSellerGoodsAttrExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int insert(MBProductSellerGoodsAttr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MBProductSellerGoodsAttr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MBProductSellerGoodsAttr> selectByExample(MBProductSellerGoodsAttrExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    MBProductSellerGoodsAttr selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MBProductSellerGoodsAttr record, @Param("example") MBProductSellerGoodsAttrExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MBProductSellerGoodsAttr record, @Param("example") MBProductSellerGoodsAttrExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MBProductSellerGoodsAttr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MBProductSellerGoodsAttr record);
}