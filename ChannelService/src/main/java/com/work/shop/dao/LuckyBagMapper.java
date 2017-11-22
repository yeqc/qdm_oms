package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.LuckyBag;
import com.work.shop.bean.LuckyBagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LuckyBagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(LuckyBagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(LuckyBagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int insert(LuckyBag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(LuckyBag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @ReadOnly
    List<LuckyBag> selectByExample(LuckyBagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    LuckyBag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") LuckyBag record, @Param("example") LuckyBagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") LuckyBag record, @Param("example") LuckyBagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(LuckyBag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(LuckyBag record);
}