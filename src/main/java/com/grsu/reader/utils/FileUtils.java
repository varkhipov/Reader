package com.grsu.reader.utils;

import java.io.File;

public class FileUtils {
	private static final ClassLoader classLoader = FileUtils.class.getClassLoader();
	public static final String separator = File.separator;

	private static final String TOMCAT_PATH = System.getProperty("catalina.base");
	private static final String APP_FILES_DIR_NAME = "app_files";

	public static final String APP_FILES_PATH = buildPath(TOMCAT_PATH, APP_FILES_DIR_NAME);
	public static final String CONFIG_FILE_PATH = buildPath(APP_FILES_PATH, "config", "config.properties");

	/**
	 * Builds system-dependent path with specific separators
	 *
	 * @param separatorPrefix
	 * @param separatorPostfix
	 * @param args
	 */
	public static String buildPath(boolean separatorPrefix, boolean separatorPostfix, String... args) {
		StringBuilder sb = new StringBuilder();
		if (separatorPrefix) {
			sb.append(separator);
		}
		for (String arg : args) {
			sb.append(arg);
			sb.append(separator);
		}
		if (!separatorPostfix) {
			sb.setLength(sb.length() - separator.length());
		}
		return sb.toString();
	}

	/**
	 * Builds system-dependent path without trailing separators
	 *
	 * @param args
	 * @return
	 */
	public static String buildPath(String... args) {
		return buildPath(false, false, args);
	}

	/**
	 * Builds system-dependent absolute path to resource file
	 *
	 * @param name
	 * @param directories
	 */
	@Deprecated
	public static String getAbsolutePathToResourceFile(String name, String... directories) {
		StringBuilder fullPath = new StringBuilder(separator);
		for (String dir : directories) {
			fullPath.append(dir);
			fullPath.append(separator);
		}
		fullPath.append(name);
		return classLoader.getResource(fullPath.toString()).getPath();
	}

}
