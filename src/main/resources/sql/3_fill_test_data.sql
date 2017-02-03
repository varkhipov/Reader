-- Dummy data for testing purposes. Need to be removed after presentation
COMMIT;
BEGIN;

INSERT INTO DEPARTMENT (name, abbreviation) VALUES ('Факультет 1', 'ф1');
INSERT INTO DEPARTMENT (name, abbreviation) VALUES ('Факультет 2', 'ф2');

INSERT INTO [GROUP] (name, department_id) VALUES ('Группа 1', 1);
INSERT INTO [GROUP] (name, department_id) VALUES ('Группа 2', 1);
INSERT INTO [GROUP] (name, department_id) VALUES ('Группа 3', 2);
INSERT INTO [GROUP] (name, department_id) VALUES ('Группа 4', 2);
INSERT INTO [GROUP] (name) VALUES ('Группа 5');

INSERT INTO LECTURER (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uid 1', 'parsed uid 1', 'Имя', 'Фамилия', 'Отчество', '123-123-4444', 'prepod@test.com');

INSERT INTO STUDENT (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uidStudent1', 'parsedUidStudent1', 'ИмяСтудент1', 'ФамилияСтудент1', 'ОтчествоСтудент1', 
	'111-111-1111', 'student1@test.com');
INSERT INTO STUDENT (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uidStudent2', 'parsedUidStudent2', 'ИмяСтудент2', 'ФамилияСтудент2', 'ОтчествоСтудент2', 
	'222-222-2222', 'student2@test.com');
INSERT INTO STUDENT (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uidStudent3', 'parsedUidStudent3', 'ИмяСтудент3', 'ФамилияСтудент3', 'ОтчествоСтудент3', 
	'333-333-3333', 'student3@test.com');
INSERT INTO STUDENT (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uidStudent4', 'parsedUidStudent4', 'ИмяСтудент4', 'ФамилияСтудент4', 'ОтчествоСтудент4', 
	'444-444-4444', 'student4@test.com');
INSERT INTO STUDENT (uid, parsed_uid, first_name, last_name, patronymic, phone, email) 
	VALUES ('uidStudent5', 'parsedUidStudent5', 'ИмяСтудент5', 'ФамилияСтудент5', 'ОтчествоСтудент5', 
	'555-555-5555', 'student5@test.com');

INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (1, 1);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (2, 1);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (3, 2);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (4, 2);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (5, 3);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (5, 5);

INSERT INTO STREAM (name) VALUES ('Поток 1');
INSERT INTO STREAM (name) VALUES ('Поток 2');
INSERT INTO STREAM (name) VALUES ('Поток 3');

INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (1, 1);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (1, 2);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (2, 3);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (3, 4);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (3, 5);

INSERT INTO COURSE (name, description, lecturer_id, stream_id) VALUES ('Курс 1', 'ОписаниеКурс1', 1, 1);
INSERT INTO COURSE (name, description, lecturer_id, stream_id) VALUES ('Курс 2', 'ОписаниеКурс2', 1, 2);

INSERT INTO LESSON (name, description, course_id, type_id)
  VALUES ('Занятие1', 'ОписаниеЗанятие1', 1, 1);
INSERT INTO LESSON (name, description, course_id, type_id)
  VALUES ('Занятие2', 'ОписаниеЗанятие2', 1, 1);
INSERT INTO LESSON (name, description, course_id, type_id, group_id)
  VALUES ('Занятие3', 'ОписаниеЗанятие3', 1, 2, 1);


INSERT INTO CLASS (date, schedule_id) VALUES ('2017-01-01', 2);
INSERT INTO CLASS (date, schedule_id) VALUES ('2017-01-02', 3);
INSERT INTO CLASS (date, schedule_id) VALUES ('2017-02-02', 4);
INSERT INTO CLASS (date, schedule_id) VALUES ('2017-02-03', 1);

INSERT INTO LESSON_CLASS (lesson_id, class_id) VALUES (1, 1);
INSERT INTO LESSON_CLASS (lesson_id, class_id) VALUES (2, 2);
INSERT INTO LESSON_CLASS (lesson_id, class_id) VALUES (3, 3);
INSERT INTO LESSON_CLASS (lesson_id, class_id) VALUES (3, 4);


INSERT INTO STUDENT_CLASS (student_id, class_id, registered, mark) VALUES (1, 2, 1, '!');
INSERT INTO STUDENT_CLASS (student_id, class_id, registered, mark) VALUES (2, 2, 1, '+');

COMMIT;
