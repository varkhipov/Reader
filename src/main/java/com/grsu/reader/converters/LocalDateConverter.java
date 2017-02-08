package com.grsu.reader.converters;

/**
 * Created by zaychick-pavel on 2/7/17.
 */

import com.grsu.reader.utils.DateUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;

@FacesConverter(value = "localDateConverter")
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		return LocalDate.parse(s, DateUtils.DATE_FORMATTER);
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		return ((LocalDate) o).format(DateUtils.DATE_FORMATTER);
	}
}
