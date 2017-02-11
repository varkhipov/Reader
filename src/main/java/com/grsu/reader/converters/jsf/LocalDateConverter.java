package com.grsu.reader.converters.jsf;

/**
 * Created by zaychick-pavel on 2/7/17.
 */

import com.grsu.reader.utils.DateUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "localDateConverter")
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (s == null) {
			return null;
		}
		if (!s.contains("T")) {
			return DateUtils.parseDate(s);
		} else {
			return LocalDateTime.parse(s);
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o == null) {
			return null;
		}
		return ((LocalDateTime)o).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
