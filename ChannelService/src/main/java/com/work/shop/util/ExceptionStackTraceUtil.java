package com.work.shop.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 打印 异常 堆栈信息类
 * 
 * @author zhoujun
 * 
 */
public class ExceptionStackTraceUtil {

	/**
	 * Get the stack trace of the exception.
	 * 
	 * @param e
	 *            The exception instance.
	 * @return The full stack trace of the exception.
	 */
	public static String getExceptionTrace(Throwable e) {

		String exceptionTrace = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		if (e != null) {
			try {
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				exceptionTrace = sw.toString();
				return exceptionTrace;
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				pw.close();
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return "No Exception";
	}
}