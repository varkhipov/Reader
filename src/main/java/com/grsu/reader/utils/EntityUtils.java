package com.grsu.reader.utils;

import com.grsu.reader.models.Entity;
import com.grsu.reader.models.Person;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {
	public static <T extends Entity> boolean entityExists(List<T> entities, int id) {
		for (Entity entity : entities) {
			if (id == entity.getId()) {
				return true;
			}
		}
		return false;
	}

	public static <T extends Entity> T getEntityById(List<T> entities, int id) {
		if (id == 0) return null;
		for (Entity entity : entities) {
			if (id == entity.getId()) {
				return (T) entity;
			}
		}
		return null;
	}

	public static <T extends Entity> List<T> getEntitiesById(List<T> entities, int id) {
		if (id == 0) return null;
		List<T> entityList = new ArrayList<>();
		for (Entity entity : entities) {
			if (id == entity.getId()) {
				entities.add((T) entity);
			}
		}
		return entityList;
	}

	public static <T extends Entity> List<T> getEntitiesExcludeId(List<T> entities, int id) {
		if (id == 0) return null;
		List<T> entityList = new ArrayList<>();
		for (Entity entity : entities) {
			if (id != entity.getId()) {
				entities.add((T) entity);
			}
		}
		return entityList;
	}

	public static boolean personExists(List<? extends Person> persons, String uid) {
		if (uid == null || uid.isEmpty()) return false;
		for (Person person : persons) {
			if (uid.equals(person.getCardUid())) {
				return true;
			}
		}
		return false;
	}

	public static <T extends Person> T getPersonByUid(List<T> persons, String uid) {
		if (uid == null || uid.isEmpty()) return null;
		for (Person person : persons) {
			if (uid.equals(person.getCardUid())) {
				return (T) person;
			}
		}
		return null;
	}
}
