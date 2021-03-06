package com.work.shop.dao.bgcontentdb;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGallery;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGalleryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BGproductGoodsGalleryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(BGproductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(BGproductGoodsGalleryExample example);

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
    int insert(BGproductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(BGproductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @ReadOnly
    List<BGproductGoodsGallery> selectByExample(BGproductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    BGproductGoodsGallery selectByPrimaryKey(Integer imgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") BGproductGoodsGallery record, @Param("example") BGproductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") BGproductGoodsGallery record, @Param("example") BGproductGoodsGalleryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(BGproductGoodsGallery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_goods_gallery
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(BGproductGoodsGallery record);
}