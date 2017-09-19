package com.supets.commons.model;

public class MYRemainTime {

	public static final long OneSecond = 1000;
	public static final long OneMinute = 60 * OneSecond;
	public static final long OneHour = 60 * OneMinute;
	public static final long OneDay = 24 * OneHour;

	public int day;
	public int hour;
	public int minute;
	public int second;
	public int msec;

	public MYRemainTime(int day, int hour, int minute, int second, int msec) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.msec = msec;
	}

}
