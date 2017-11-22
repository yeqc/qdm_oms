package com.work.shop.timer;

import java.util.Timer;
import java.util.TimerTask;

public class GoodsStatusSynchronousTimer extends Timer {

	public GoodsStatusSynchronousTimer(TimerTask t, long delay, long period) {
		super.schedule(t, delay, period);
	}

}
