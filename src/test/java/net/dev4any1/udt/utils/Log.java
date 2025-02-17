package net.dev4any1.udt.utils;

import java.util.Date;

public class Log {
	public static void info(Object o, String message, long duration, int testCount) {
		System.out.println(String.format("[%s] %s (%s) %d %s in %d ms. Rate: %d msg./ms.",
				new Date(System.currentTimeMillis()).toString(), Thread.currentThread().getName(),
				o.getClass().getSimpleName(), testCount, message, duration, (testCount / duration)));
	}
}
