package com.work.shop.mq.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.oms.activemq.listener.DynamicListenerManager;
import com.work.shop.util.Constants;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.service.impl.ShopServiecImpl;

public class MqContextLoaderListener extends ContextLoaderListener {

	private final Logger logger = Logger.getLogger(MqContextLoaderListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ApplicationContext ac = getCurrentWebApplicationContext();

		DynamicListenerManager manager = ac.getBean(DynamicListenerManager.class);
		InterfacePropertiesService propertiesService = ac.getBean(InterfacePropertiesService.class);
		ShopServiecImpl shopServiecImpl = ac.getBean(ShopServiecImpl.class);
		ChannelShopExample example = new ChannelShopExample();
		ChannelShopExample.Criteria criteria = example.or();
		criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
		criteria.andShopChannelEqualTo(Byte.valueOf("1"));
		List<com.work.shop.bean.ChannelShop> channelShops = shopServiecImpl.selectOnlineOnsaleChannelShop();
		if (!StringUtil.isListNotNull(channelShops)){
			return;
		}
		SynchItemListener itemListener = ac.getBean(SynchItemListener.class);
		SynchStockListener stockListener = ac.getBean(SynchStockListener.class);
		try {
			for (ChannelShop shopbean : channelShops) {
				if (shopbean == null)
					continue;
				String shopCode = shopbean.getShopCode();
				if (StringUtil.isNotNull(shopCode)) {
					String itemQueueName = Constants.QUEUE_FIX_ITEM + shopCode;
					manager.registeQueueLintener(itemQueueName, itemListener, "2");
					String stockQueueName = shopCode + Constants.QUEUE_FIX_STOCK;
					manager.registeQueueLintener(stockQueueName, stockListener, "1");
					propertiesService.syncSecurityInfo(shopCode);
				}
			}
		} catch (Exception e) {
			logger.error("同步MQ注册异常：" + e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}

}
