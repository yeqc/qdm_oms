package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GiftsGoodsListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiftsGoodsListMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(GiftsGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(GiftsGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int insert(GiftsGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(GiftsGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @ReadOnly
    List<GiftsGoodsList> selectByExample(GiftsGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    GiftsGoodsList selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") GiftsGoodsList record, @Param("example") GiftsGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") GiftsGoodsList record, @Param("example") GiftsGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(GiftsGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gifts_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(GiftsGoodsList record);
}