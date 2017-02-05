-- Delete application tables

COMMIT;
BEGIN;

DROP TABLE IF EXISTS STUDENT_GROUP;
DROP TABLE IF EXISTS STREAM_GROUP;
DROP TABLE IF EXISTS STUDENT_CLASS;

DROP TABLE IF EXISTS CLASS;

DROP TABLE IF EXISTS LESSON;
DROP TABLE IF EXISTS LESSON_TYPE;

DROP TABLE IF EXISTS COURSE;

DROP TABLE IF EXISTS [GROUP];
DROP TABLE IF EXISTS GROUP_TYPE;

DROP TABLE IF EXISTS SCHEDULE;
DROP TABLE IF EXISTS SCHEDULE_VERSION;

DROP TABLE IF EXISTS NOTE;
DROP TABLE IF EXISTS NOTE_TYPE;

DROP TABLE IF EXISTS LECTURER;
DROP TABLE IF EXISTS DEPARTMENT;
DROP TABLE IF EXISTS STUDENT;
DROP TABLE IF EXISTS STREAM;

COMMIT;