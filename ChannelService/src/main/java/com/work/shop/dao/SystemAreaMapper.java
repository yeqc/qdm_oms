package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.SystemArea;
import com.work.shop.bean.SystemAreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemAreaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int insert(SystemArea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(SystemArea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SystemArea> selectByExampleWithBLOBs(SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SystemArea> selectByExample(SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    SystemArea selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") SystemArea record, @Param("example") SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") SystemArea record, @Param("example") SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") SystemArea record, @Param("example") SystemAreaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(SystemArea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(SystemArea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_area
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(SystemArea record);
}