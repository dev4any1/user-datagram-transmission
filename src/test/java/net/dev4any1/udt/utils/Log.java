package net.dev4any1.udt.utils;

import java.util.Date;

public class Log {
	public static void info(Object o, String message) {
		System.out.println("[" + new Date(System.currentTimeMillis()).toString() + "] "
				+ Thread.currentThread().getName() + " (" + o.getClass().getSimpleName() + ") " + message);
	}
}
