-- Dummy data for testing purposes. Need to be removed after presentation
BEGIN;

INSERT INTO Discipline (name) VALUES ('Psychology');
INSERT INTO Discipline (name) VALUES ('Психология');

INSERT INTO [Group] (name) VALUES ('Group_1');
INSERT INTO [Group] (name) VALUES ('Группа_2');

INSERT INTO Lecturer (name, surname) VALUES ('Lecturer', '1');
INSERT INTO Lecturer (uid, name, surname) VALUES ('TEST_LECTURER_UID_1', 'Преподаватель', '2');

INSERT INTO Student (uid, name, surname) VALUES ('TEST_UID_1', 'Student w/o group', '_1');
INSERT INTO Student (uid, name, surname) VALUES ('TEST_UID_2', 'Студент без группы', '2');
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_3', 'Student w group', '_1', 1);
INSERT INTO Student (uid, name, surname, groupId) VALUES ('TEST_UID_4', 'Студент в группе', '_2', 2);

INSERT INTO Lecture (name, timeBefore, timeAfter, dateTime, scheduleId, disciplineId, lecturerId, groupId)
VALUES ('lecture name 1', '', '', '2017-01-01T10:10:10.100', 1, 1, 1, 1);
INSERT INTO Lecture (name, timeBefore, timeAfter, dateTime, scheduleId, disciplineId, lecturerId, groupId)
VALUES ('какая-то лекция 2', '', '', '2017-01-05T17:21:08.519', 2, 2, 2, 2);

INSERT INTO Lecture_Student (lectureId, studentId) VALUES (1, 1);
INSERT INTO Lecture_Student (lectureId, studentId) VALUES (1, 3);
INSERT INTO Lecture_Student (lectureId, studentId) VALUES (2, 2);
INSERT INTO Lecture_Student (lectureId, studentId) VALUES (2, 4);

COMMIT;
