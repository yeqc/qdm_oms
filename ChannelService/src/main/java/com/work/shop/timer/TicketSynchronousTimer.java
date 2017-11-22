package com.work.shop.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TicketSynchronousTimer extends Timer {

	public TicketSynchronousTimer(TimerTask t, long delay, long period) {
		super.schedule(t, delay, period);
	}

}
