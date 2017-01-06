package com.grsu.reader.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.grsu.reader.utils.FileUtils.*;

public class PropertyUtils {
	public static String getProperty(String name) {
		Properties props = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(CONFIG_FILE_PATH);
			props.load(input);

			return props.getProperty(name);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void setProperty(String name, String value) {
		Properties props = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(CONFIG_FILE_PATH);
			props.load(input);

			props.setProperty(name, value);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
