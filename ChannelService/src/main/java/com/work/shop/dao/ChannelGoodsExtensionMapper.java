package com.work.shop.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.ChannelGoodsExtension;
import com.work.shop.bean.ChannelGoodsExtensionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChannelGoodsExtensionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int insert(ChannelGoodsExtension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ChannelGoodsExtension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ChannelGoodsExtension> selectByExampleWithBLOBs(ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ChannelGoodsExtension> selectByExample(ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    ChannelGoodsExtension selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ChannelGoodsExtension record, @Param("example") ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") ChannelGoodsExtension record, @Param("example") ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ChannelGoodsExtension record, @Param("example") ChannelGoodsExtensionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(ChannelGoodsExtension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(ChannelGoodsExtension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_goods_extension
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(ChannelGoodsExtension record);
}