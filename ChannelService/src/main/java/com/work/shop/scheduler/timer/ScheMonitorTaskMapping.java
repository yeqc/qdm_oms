package com.work.shop.scheduler.timer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.banggo.scheduler.exception.NoSuchTaskException;
import com.banggo.scheduler.mapping.TaskMapping;
import com.banggo.scheduler.task.Task;
import com.banggo.scheduler.task.TaskExecuteRequest;

@Component
public class ScheMonitorTaskMapping implements TaskMapping, ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Task getTask(TaskExecuteRequest request) throws NoSuchTaskException {
		String bean = request.getParam("beanName");

		System.out.println("beanName======" + bean);
		if (StringUtils.isBlank(bean)) {
			return null;
		}
		Task task = (Task) applicationContext.getBean(bean);
		return task;
	}

	@Override
	public boolean support(TaskExecuteRequest request) {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ScheMonitorTaskMapping.applicationContext = applicationContext;
	}
}
