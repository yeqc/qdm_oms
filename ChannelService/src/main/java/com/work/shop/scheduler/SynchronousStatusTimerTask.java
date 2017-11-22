/**   
 * @author Haijing Chen
 * @date Aug 7, 2014 3:10:54 PM 
 */
package com.work.shop.scheduler;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.banggo.scheduler.exception.FatalException;
import com.banggo.scheduler.exception.WarnningException;
import com.banggo.scheduler.task.RunInOneNodeTask;
import com.banggo.scheduler.task.TaskExecuteRequest;
import com.work.shop.scheduler.service.SynchronousStatusService;
import com.work.shop.task.ISynchronousStatusTimerTask;
import com.work.shop.util.StringUtil;

/**
 * @author derek
 * 
 */
@Component("synchronousStatusTimerTask")
public class SynchronousStatusTimerTask extends RunInOneNodeTask implements ISynchronousStatusTimerTask {

	private Logger logger = Logger.getLogger(SynchronousStatusTimerTask.class);

	@Resource(name = "synchronousStatusService")
	private SynchronousStatusService synchronousStatusService;


	@Override
	public void execute(TaskExecuteRequest request) throws WarnningException, FatalException {
//		String shopCodeParam = request.getParam("shopCodes");
//		logger.debug("调度中心操作：渠道店铺商品状态同步shopCodeParam =  " + shopCodeParam);
//		if (StringUtil.isNotEmpty(shopCodeParam)) {
//			String[] shopCodes = shopCodeParam.split(",");
//			synchronousStatusService.synchronousStatus(shopCodes);
//		}
		synchronousStatus(request.getParams());
	}

	@Override
	public void synchronousStatus(Map<String, String> para) {
		String shopCodeParam = para.get("shopCodeParam");
		logger.debug("调度中心操作：渠道店铺商品状态同步shopCodeParam =  " + shopCodeParam);
		if (StringUtil.isNotEmpty(shopCodeParam)) {
			try {
				String[] shopCodes = shopCodeParam.split(",");
				synchronousStatusService.synchronousStatus(shopCodes);
			} catch (Exception e) {
				logger.error("execute异常：", e);
			}
	     }
	}

}
