package com.work.shop.api.handler;

import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.api.handler.ioc.SpringApplicationContextAware;
import com.work.shop.util.Constants;

public class HandlerBuildFactory {

    //    private Map<String, ChannelApiHandler> handlers;

    @Resource SpringApplicationContextAware springApplicationContextAware;

    private Map<String, String>             handlers;

    public ChannelApiHandler createrChannelApi(Map<String, String> secretInforMap) {
        String channelCode = secretInforMap.get(Constants.CHANNEL_CODE);
        String shopCode = secretInforMap.get(Constants.SHOP_CODE);
        ChannelApiHandler handler = findHandle(channelCode, shopCode);
        handler.buildApiClient(secretInforMap);
        return handler;
    }

    @SuppressWarnings("static-access")
    private ChannelApiHandler findHandle(String channelCode, String shopCode) {
        String handleSpringId = handlers.get(channelCode);
        return springApplicationContextAware.getBean(handleSpringId, ChannelApiHandler.class);
    }

    public void setHandlers(Map<String, String> handlers) {
        this.handlers = handlers;
    }

}
