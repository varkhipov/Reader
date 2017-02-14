package com.grsu.reader.utils;

import com.grsu.reader.entities.AssistantEntity;
import com.grsu.reader.entities.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityUtils {
	public static <T extends AssistantEntity> boolean entityExists(List<T> entities, int id) {
		for (AssistantEntity entity : entities) {
			if (id == entity.getId()) {
				return true;
			}
		}
		return false;
	}

	public static <T extends AssistantEntity> T getEntityById(List<T> entities, int id) {
		if (id == 0) return null;
		for (AssistantEntity entity : entities) {
			if (id == entity.getId()) {
				return (T) entity;
			}
		}
		return null;
	}

	public static <T extends AssistantEntity> List<T> getEntitiesById(List<T> entities, int id) {
		if (id == 0) return null;
		List<T> entityList = new ArrayList<>();
		for (AssistantEntity entity : entities) {
			if (id == entity.getId()) {
				entities.add((T) entity);
			}
		}
		return entityList;
	}

	public static <T extends AssistantEntity> List<T> getEntitiesExcludeId(List<T> entities, int id) {
		if (id == 0) return null;
		List<T> entityList = new ArrayList<>();
		for (AssistantEntity entity : entities) {
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

	public static <T extends AssistantEntity> boolean compareEntityLists(List<T> first, List<T> second) {
		if (first == null && second == null) {
			return true;
		}
		if (first == null || second == null || first.size() != second.size()) {
			return false;
		}

		for (int i = 0; i < first.size(); i++) {
			if (!compareEntity(first.get(i), second.get(i))) {
				return false;
			}
		}

		return true;
	}

	public static <T extends AssistantEntity> boolean compareEntity(T first, T second) {
		if (first == null && second == null) {
			return true;
		}
		if (first == null || second == null || !Objects.equals(first.getId(), second.getId())) {
			return false;
		}
		return true;
	}
}
