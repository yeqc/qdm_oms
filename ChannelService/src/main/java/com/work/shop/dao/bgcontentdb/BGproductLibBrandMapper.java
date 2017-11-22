package com.work.shop.dao.bgcontentdb;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.bgcontentdb.BGproductLibBrand;
import com.work.shop.bean.bgcontentdb.BGproductLibBrandExample;
import com.work.shop.bean.bgcontentdb.BGproductLibBrandWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BGproductLibBrandMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(String brandCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int insert(BGproductLibBrandWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(BGproductLibBrandWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @ReadOnly
    List<BGproductLibBrandWithBLOBs> selectByExampleWithBLOBs(BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @ReadOnly
    List<BGproductLibBrand> selectByExample(BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    BGproductLibBrandWithBLOBs selectByPrimaryKey(String brandCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") BGproductLibBrandWithBLOBs record, @Param("example") BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") BGproductLibBrandWithBLOBs record, @Param("example") BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") BGproductLibBrand record, @Param("example") BGproductLibBrandExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(BGproductLibBrandWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(BGproductLibBrandWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_brand
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(BGproductLibBrand record);
}