package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.ProductGoodsGallery;
import com.work.shop.bean.ProductGoodsGalleryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductGoodsGalleryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ProductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ProductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer imgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int insert(ProductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ProductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ProductGoodsGallery> selectByExample(ProductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    ProductGoodsGallery selectByPrimaryKey(Integer imgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ProductGoodsGallery record, @Param("example") ProductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ProductGoodsGallery record, @Param("example") ProductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(ProductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(ProductGoodsGallery record);
}