package com.work.shop.dao.mbproduct;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.mbproduct.MBProductSellerGoods;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MBProductSellerGoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer productId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int insert(MBProductSellerGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MBProductSellerGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MBProductSellerGoods> selectByExampleWithBLOBs(MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MBProductSellerGoods> selectByExample(MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    MBProductSellerGoods selectByPrimaryKey(Integer productId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MBProductSellerGoods record, @Param("example") MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") MBProductSellerGoods record, @Param("example") MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MBProductSellerGoods record, @Param("example") MBProductSellerGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MBProductSellerGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(MBProductSellerGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MBProductSellerGoods record);
}