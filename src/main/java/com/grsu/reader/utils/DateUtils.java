package com.grsu.reader.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	private static final String FORMAT_TIME = "HH:mm";

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME);

	private static String formatDate(LocalDateTime date, DateTimeFormatter formatter) {
		return date.format(formatter);
	}

	public static String formatDate(LocalDateTime date) {
		return formatDate(date, DATE_FORMATTER);
	}

	public static String formatTime(LocalDateTime date) {
		return formatDate(date, TIME_FORMATTER);
	}

	private static LocalDateTime parseDate(String date, DateTimeFormatter formatter) {
		return LocalDateTime.parse(date, formatter);
	}

	public static LocalDateTime parseDate(String date) {
		return parseDate(date, DATE_FORMATTER);
	}

	public static LocalDateTime parseTime(String date) {
		return parseDate(date, TIME_FORMATTER);
	}
}
