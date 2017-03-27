package com.grsu.reader.converters.db;

import com.grsu.reader.models.LessonType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by pavel on 3/26/17.
 */
@Converter(autoApply = true)
public class LessonTypeAttributeConverter implements AttributeConverter<LessonType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(LessonType lessonType) {
		return lessonType.getCode();
	}

	@Override
	public LessonType convertToEntityAttribute(Integer code) {
		return LessonType.getLessonTypeByCode(code);
	}
}
