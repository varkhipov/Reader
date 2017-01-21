package com.grsu.reader.converters;

import com.grsu.reader.models.Entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 * http://stackoverflow.com/a/23213333
 */
@FacesConverter(value = "entityConverter", forClass = Entity.class)
public class EntityConverter implements Converter {

	private static Map<Entity, Integer> entities = new WeakHashMap<>();

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object entity) {
		synchronized (entities) {
			if (!entities.containsKey(entity)) {
				Entity e = (Entity) entity;
				entities.put(e, e.getId());
				return String.valueOf(e.getId());
			} else {
				return entities.get(entity).toString();
			}
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id) {
		for (Entry<Entity, Integer> entry : entities.entrySet()) {
			if (entry.getValue().toString().equals(id)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
