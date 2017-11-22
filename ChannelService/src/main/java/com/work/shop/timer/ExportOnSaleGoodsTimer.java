package com.work.shop.timer;

import java.util.Timer;
import java.util.TimerTask;

public class ExportOnSaleGoodsTimer extends Timer {
	
	//指定定时执行时间
	public ExportOnSaleGoodsTimer(TimerTask t, long delay, long period) {
		super.schedule(t, delay, period);
	}

}
