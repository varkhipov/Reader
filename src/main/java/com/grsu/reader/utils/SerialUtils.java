package com.grsu.reader.utils;

import jssc.SerialPortList;

import java.util.regex.Pattern;

public class SerialUtils {
	public static final String CARD_UID_PREFIX = "Card UID:";
	public static final String GET_INFO = "get info";
	public static final String SET_START = "set start";
	public static final String STATUS_OK = "status: ok";
	public static final String STATUS_OL = "status: ol";
	public static final String STATUS_ER = "status: er";
	public static final String STATUS_EL = "status: el";

	public static String[] getPortNames() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return SerialPortList.getPortNames("/dev/", Pattern.compile("cu."));
		}
		return SerialPortList.getPortNames();
	}


}
