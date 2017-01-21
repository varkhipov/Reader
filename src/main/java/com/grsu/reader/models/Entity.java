package com.grsu.reader.models;

import javax.faces.model.SelectItem;

public abstract class Entity {
	public abstract int getId();
	public abstract SelectItem getSelectItem();
}
