package com.work.shop.dao;

import java.util.List;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.vo.ChannelShopVo;

public interface ChannelShopDefineMapper {

    @ReadOnly
    List<ChannelShopVo> selectByVoExampleIncludeChild(ChannelShopExample example);
    
    @ReadOnly
    List<ChannelShopVo> selectByVoExampleExcludeChild(ChannelShopExample example);

}