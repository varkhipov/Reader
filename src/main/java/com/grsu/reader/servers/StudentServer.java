package com.grsu.reader.servers;

import org.primefaces.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by zaychick-pavel on 2/21/17.
 */
public class StudentServer {
	private static final String PERSONNEL_NUMBER_URL = "http://api.grsu.by/1.x/app3/getStudentByCard?cardid=";
	private static final String PERSONNEL_NUMBER_NAME = "TN";

	public static String getPersonnelNumber(Integer cardId) {
		JSONObject json = readJsonFromUrl(PERSONNEL_NUMBER_URL + Integer.toString(cardId));
		if (json != null) {
			return (String) json.get(PERSONNEL_NUMBER_NAME);
		}
		return null;
	}


	public static void main(String[] args) {
		System.out.println(getPersonnelNumber(-1017756570));
	}

	private static JSONObject readJsonFromUrl(String url) {
		try {
			InputStream is = new URL(url).openStream();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				String jsonText = readAll(rd).replaceAll("\\[", "").replaceAll("\\]", "");
				return new JSONObject(jsonText);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
