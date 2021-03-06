package com.work.shop.dao.mbproduct;

import com.work.shop.bean.mbproduct.ProductChannelGoodsPrice;
import com.work.shop.bean.mbproduct.ProductChannelGoodsPriceExample;
import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductChannelGoodsPriceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ProductChannelGoodsPriceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ProductChannelGoodsPriceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int insert(ProductChannelGoodsPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ProductChannelGoodsPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ProductChannelGoodsPrice> selectByExample(ProductChannelGoodsPriceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    ProductChannelGoodsPrice selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ProductChannelGoodsPrice record, @Param("example") ProductChannelGoodsPriceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ProductChannelGoodsPrice record, @Param("example") ProductChannelGoodsPriceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(ProductChannelGoodsPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_price
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(ProductChannelGoodsPrice record);
}