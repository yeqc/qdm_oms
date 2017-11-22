package com.work.shop.dao.bgcontentdb;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.bgcontentdb.BGproductLibCategory;
import com.work.shop.bean.bgcontentdb.BGproductLibCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BGproductLibCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(BGproductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(BGproductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Short catId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int insert(BGproductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(BGproductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @ReadOnly
    List<BGproductLibCategory> selectByExample(BGproductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    BGproductLibCategory selectByPrimaryKey(Short catId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") BGproductLibCategory record, @Param("example") BGproductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") BGproductLibCategory record, @Param("example") BGproductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(BGproductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(BGproductLibCategory record);
}