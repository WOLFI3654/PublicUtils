package de.wolfi.utils;

import java.util.Calendar;

public class TimeUtil {

	
	public static long fromTime(String time){
		String[] split = time.split(":");
		Calendar d = Calendar.getInstance();
		d.set(Integer.parseInt(split[0]), Integer.parseInt(split[1])-1, Integer.parseInt(split[2]), Integer.parseInt(split[3]),  Integer.parseInt(split[4]), Integer.parseInt( split[5]));
		return d.getTimeInMillis();
	}
	
}
