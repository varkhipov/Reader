-- Dummy data for testing purposes. Need to be removed after presentation
COMMIT;
BEGIN;

INSERT INTO Discipline (name) VALUES ('Psychology');
INSERT INTO Discipline (name) VALUES ('Психология');

INSERT INTO Faculty (name) VALUES ('FaMI');
INSERT INTO Faculty (name) VALUES ('ФаМИ');

INSERT INTO Stream (name) VALUES ('Some Stream');
INSERT INTO Stream (name) VALUES ('Какой-то поток');

INSERT INTO [Group] (name) VALUES ('Group w/o faculty');
INSERT INTO [Group] (name) VALUES ('Группа без факультета');
INSERT INTO [Group] (name, facultyId) VALUES ('Group 2 w faculty', 1);
INSERT INTO [Group] (name, facultyId) VALUES ('Группа 2 с факультетом', 2);

INSERT INTO Stream_Group (streamId, groupId) VALUES (1, 1);
INSERT INTO Stream_Group (streamId, groupId) VALUES (1, 3);
INSERT INTO Stream_Group (streamId, groupId) VALUES (2, 2);
INSERT INTO Stream_Group (streamId, groupId) VALUES (2, 4);

INSERT INTO Lecturer (name, surname) VALUES ('Lecturer', '1');
INSERT INTO Lecturer (uid, name, surname) VALUES ('TEST_LECTURER_UID_1', 'Преподаватель', '2');

INSERT INTO Student (uid, name, surname) VALUES ('TEST_UID_1', 'Student w/o group', '_1');
INSERT INTO Student (uid, name, surname) VALUES ('TEST_UID_2', 'Студент без группы', '2');
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_3', 'Student w group', '_1', 1);
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_4', 'Студент в группе', '_2', 2);
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_5', 'Студент в группе', '_3', 3);
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_6', 'Студент в группе', '_4', 4);
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_7', 'Студент в группе (прогул)', '_5', 4);

INSERT INTO Lesson (name, createDate, disciplineId, lecturerId, streamId)
  VALUES ('lesson name 1', '2017-01-01T10:10:10.100', 1, 1, 1);
INSERT INTO Lesson (name, createDate, disciplineId, lecturerId, streamId)
  VALUES ('какая-то лекция 1', '2017-01-05T13:21:08.519', 2, 2, 2);

INSERT INTO Class (date, scheduleId) VALUES ('2017-01-01', 2);
INSERT INTO Class (date, scheduleId) VALUES ('2016-12-13', 4);
INSERT INTO Class (date, scheduleId) VALUES ('2016-12-14', 5);

INSERT INTO Lesson_Class (lessonId, classId) VALUES (1, 1);
INSERT INTO Lesson_Class (lessonId, classId) VALUES (2, 2);
INSERT INTO Lesson_Class (lessonId, classId) VALUES (2, 3);

INSERT INTO Student_Class (studentId, classId) VALUES (1, 2);
INSERT INTO Student_Class (studentId, classId) VALUES (2, 3);
INSERT INTO Student_Class (studentId, classId) VALUES (3, 1);
INSERT INTO Student_Class (studentId, classId) VALUES (4, 2);
INSERT INTO Student_Class (studentId, classId) VALUES (5, 1);
INSERT INTO Student_Class (studentId, classId) VALUES (6, 3);

COMMIT;
