package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.vo.TicketInfoVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TicketInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(TicketInfoExample example);

    //下面两个方法是荀海加在generator生成文件里的，不是我加的，我也不想帮他擦屁股，我就把他的方法保留在这里了
    @ReadOnly
    List<TicketInfoVo> selectByVo(Map<String,Object> params);
    
    @ReadOnly
    int countByVo (Map<String,Object> params);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(TicketInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int insert(TicketInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(TicketInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @ReadOnly
    List<TicketInfo> selectByExample(TicketInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    TicketInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") TicketInfo record, @Param("example") TicketInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") TicketInfo record, @Param("example") TicketInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(TicketInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ticket_info
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(TicketInfo record);
    
}