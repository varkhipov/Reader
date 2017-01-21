package com.grsu.reader.utils;

import com.grsu.reader.models.Entity;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public class EntityUtils {
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

	public static <T extends Entity> List<SelectItem> generateSelectItems(List<T> entities) {
		List<SelectItem> items = new ArrayList<>();
		for (Entity entity : entities) {
			items.add(entity.getSelectItem());
		}
		return items;
	}
}
