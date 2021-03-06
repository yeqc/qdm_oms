package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.ProductLibCategory;
import com.work.shop.bean.ProductLibCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductLibCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ProductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ProductLibCategoryExample example);

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
    int insert(ProductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ProductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ProductLibCategory> selectByExample(ProductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    ProductLibCategory selectByPrimaryKey(Short catId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ProductLibCategory record, @Param("example") ProductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ProductLibCategory record, @Param("example") ProductLibCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(ProductLibCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_category
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(ProductLibCategory record);
}