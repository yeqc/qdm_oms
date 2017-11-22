package com.work.shop.dao.mbproduct;

import com.work.shop.bean.mbproduct.ProductChannelGoodsBarcode;
import com.work.shop.bean.mbproduct.ProductChannelGoodsBarcodeExample;
import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductChannelGoodsBarcodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ProductChannelGoodsBarcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ProductChannelGoodsBarcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int insert(ProductChannelGoodsBarcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ProductChannelGoodsBarcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ProductChannelGoodsBarcode> selectByExample(ProductChannelGoodsBarcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    ProductChannelGoodsBarcode selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ProductChannelGoodsBarcode record, @Param("example") ProductChannelGoodsBarcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ProductChannelGoodsBarcode record, @Param("example") ProductChannelGoodsBarcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(ProductChannelGoodsBarcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_channel_goods_barcode
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(ProductChannelGoodsBarcode record);
}