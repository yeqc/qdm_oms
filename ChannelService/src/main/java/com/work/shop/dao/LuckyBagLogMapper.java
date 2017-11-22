package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.LuckyBagLog;
import com.work.shop.bean.LuckyBagLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LuckyBagLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int insert(LuckyBagLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(LuckyBagLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @ReadOnly
    List<LuckyBagLog> selectByExampleWithBLOBs(LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @ReadOnly
    List<LuckyBagLog> selectByExample(LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    LuckyBagLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") LuckyBagLog record, @Param("example") LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") LuckyBagLog record, @Param("example") LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") LuckyBagLog record, @Param("example") LuckyBagLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(LuckyBagLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(LuckyBagLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lucky_bag_log
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(LuckyBagLog record);
}