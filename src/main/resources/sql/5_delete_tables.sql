-- Delete application tables

COMMIT;
BEGIN;

DROP TABLE IF EXISTS Note;
DROP TABLE IF EXISTS Student_Class;
DROP TABLE IF EXISTS Lesson_Class;
DROP TABLE IF EXISTS Class;
DROP TABLE IF EXISTS Lesson;
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Lecturer;
DROP TABLE IF EXISTS Stream_Group;
DROP TABLE IF EXISTS [Group];
DROP TABLE IF EXISTS Stream;
DROP TABLE IF EXISTS Faculty;
DROP TABLE IF EXISTS Discipline;
DROP TABLE IF EXISTS Schedule;

COMMIT;