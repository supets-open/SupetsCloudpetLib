package com.supets.commons.utils;

import java.text.DecimalFormat;

public class MYDecimalFormat {

	private static final String PRICE_PATTERN = "#.##";
	//private static final String DISCOUNT_PATTERN = "0.0";

	public static String formatPrice(double value) {
		return format(value, PRICE_PATTERN);
	}

	public static String formatPrice(float value) {
		return format(value, PRICE_PATTERN);
	}

	public static String formatPrice(String value) {
		return format(value, PRICE_PATTERN);
	}

	public static String formatSinglePrice(double price) {
		int ceil = (int) Math.ceil(price);
		return ceil + "";
	}

	public static String formatDiscount(double discount) {
		int x = (int) (discount * 10000);
		int y = x % 1000;
		x -= y;
		x += (y > 0 && x < 99000) ? 1000 : 0;
		double result = 1.0 * x / 10000;
		return result + "";
	}

	private static String format(double value, String pattern) {
		DecimalFormat format = new DecimalFormat(pattern);
		return format.format(value);
	}

	private static String format(String value, String pattern) {
		try {
			return format(Double.parseDouble(value), pattern);
		} catch (Exception e) {
			return value;
		}
	}

}
