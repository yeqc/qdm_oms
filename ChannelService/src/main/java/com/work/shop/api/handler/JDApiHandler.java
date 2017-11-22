package com.work.shop.api.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.bean.LocalItemQuery;

@Service("jDApiHandler")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JDApiHandler extends ChannelApiHandler {

	private Logger logger = Logger.getLogger(JDApiHandler.class);

	@Override
	public ApiResultVO updatePrice(ItemUpdate itemUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO updateGoodsName(ItemUpdate itemUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO shelvesUpOrDown(ItemUpdate itemUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO updateItemStock(ItemUpdate itemUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO getBaseData(ItemQuery itemQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO addItem(ItemAdd itemAdd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO searchItemPage(LocalItemQuery query) {
		// TODO Auto-generated method stub
		return null;
	}
}
