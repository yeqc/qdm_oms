package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.GroupGoodsList;
import com.work.shop.bean.GroupGoodsListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GroupGoodsListMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(GroupGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(GroupGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int insert(GroupGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(GroupGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @ReadOnly
    List<GroupGoodsList> selectByExample(GroupGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    GroupGoodsList selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") GroupGoodsList record, @Param("example") GroupGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") GroupGoodsList record, @Param("example") GroupGoodsListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(GroupGoodsList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_goods_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(GroupGoodsList record);
}