package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.PromotionsListInfor;
import com.work.shop.bean.PromotionsListInforExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PromotionsListInforMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(PromotionsListInforExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(PromotionsListInforExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int insert(PromotionsListInfor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(PromotionsListInfor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @ReadOnly
    List<PromotionsListInfor> selectByExample(PromotionsListInforExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    PromotionsListInfor selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") PromotionsListInfor record, @Param("example") PromotionsListInforExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") PromotionsListInfor record, @Param("example") PromotionsListInforExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(PromotionsListInfor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promotions_list_infor
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(PromotionsListInfor record);
}