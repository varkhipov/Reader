package com.grsu.reader.servers;

import com.grsu.reader.utils.FileUtils;
import org.primefaces.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by zaychick-pavel on 2/21/17.
 */
public class StudentServer {
	private static final String PERSONNEL_NUMBER_URL = "http://api.grsu.by/1.x/app3/getStudentByCard?cardid=";
	private static final String PERSONNEL_NUMBER_NAME = "TN";
	private static final String STUDENT_PHOTO_URL = "https://intra.grsu.by/photos/";
	private static final String STUDENT_PHOTO_EXTENSION = ".jpg";

	public static String getPersonnelNumber(Integer cardId) {
		JSONObject json = readJsonFromUrl(PERSONNEL_NUMBER_URL + Integer.toString(cardId));
		if (json != null) {
			return (String) json.get(PERSONNEL_NUMBER_NAME);
		}
		return null;
	}

	public static boolean storeImage(String personnelNumber, String cardUid) {
		try {
			URL url = new URL(STUDENT_PHOTO_URL + personnelNumber + STUDENT_PHOTO_EXTENSION);
			BufferedImage image = ImageIO.read(url);

			ImageIO.write(image, "jpg", FileUtils.getFile(FileUtils.STUDENTS_PHOTO_FOLDER_PATH, cardUid, FileUtils.STUDENTS_PHOTO_EXTENSION));
			return true;
		} catch (IOException e) {
			System.out.println(STUDENT_PHOTO_URL + personnelNumber + STUDENT_PHOTO_EXTENSION);
			e.printStackTrace();
		}
		return false;
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
