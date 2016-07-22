package org.h819.commons.date;

import org.apache.commons.lang.ArrayUtils;

public class CalUtils {

	final static int DOM[] = { 31, 28, 31, 30, /* jan feb mar apr */
	31, 30, 31, 31, /* may jun jul aug */
	30, 31, 30, 31 /* sep oct nov dec */
	};

	final static String weekEnNameArray[] = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };

	final static String weekCnNameArray[] = { "星期日", "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六" };

	final static String[] cnMonths = { "一月", "二月", "三月", "四月", "五月", "六月",
			"七月", "八月", "九月", "十月", "十一月", "十二月" };

	public static int[] getDaysInMonths() {
		return DOM.clone();
	}

	public static int getDaysInMonth(int monthNum) {
		if (monthNum < 0) {
			throw new IllegalArgumentException(
					"Month number must be non-negative");
		}
		if (monthNum >= 12) {
			throw new IndexOutOfBoundsException(
					"There are 12 months in a year, so monthNum must be in 0..11");
		}
		return DOM[monthNum];
	}

	public static String getWeekCNName(String weekEnName) {
		return weekCnNameArray[ArrayUtils.indexOf(weekEnNameArray, weekEnName)];
	}

	public static String getWeekENName(String weekCnName) {
		return weekEnNameArray[ArrayUtils.indexOf(weekCnNameArray, weekCnName)];
	}
}
