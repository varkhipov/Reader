COMMIT;
BEGIN;

-- LECTURER
CREATE TABLE Lecturer (
  id            INTEGER PRIMARY KEY,
  uid           TEXT UNIQUE,
  name          TEXT NOT NULL,
  surname       TEXT NOT NULL,
  patronymic    TEXT,
  phone         TEXT,
  email         TEXT,
  notes         TEXT,
  photo         BLOB
);

-- SCHEDULE
CREATE TABLE Schedule (
  id            INTEGER PRIMARY KEY,
  begin         TEXT NOT NULL,
  end           TEXT NOT NULL,
  number        INTEGER NOT NULL
);

-- DISCIPLINE
CREATE TABLE Discipline (
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL
);

-- FACULTY
CREATE TABLE Faculty (
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL
);

-- STREAM
CREATE TABLE Stream (
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL
);

-- GROUP (used brackets because GROUP is reserved keyword)
CREATE TABLE [Group] (
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL,
  facultyId     INTEGER,
  FOREIGN KEY (facultyId)     REFERENCES Faculty(id)
);

-- STREAM-GROUP
CREATE TABLE Stream_Group (
  streamId      INTEGER NOT NULL,
  groupId       INTEGER NOT NULL,
  FOREIGN KEY (streamId)      REFERENCES Stream(id),
  FOREIGN KEY (groupId)       REFERENCES [Group](id)
);

-- STUDENT
CREATE TABLE Student (
  id            INTEGER PRIMARY KEY,
  uid           TEXT NOT NULL UNIQUE,
  name          TEXT NOT NULL,
  surname       TEXT NOT NULL,
  patronymic    TEXT,
  phone         TEXT,
  email         TEXT,
  notes         TEXT,
  photo         BLOB
);

-- STUDENT-GROUP
CREATE TABLE Student_Group (
  studentId     INTEGER NOT NULL,
  groupId       INTEGER NOT NULL,
  FOREIGN KEY (studentId)     REFERENCES Student(id),
  FOREIGN KEY (groupId)       REFERENCES [Group](id)
);

-- LESSON
CREATE TABLE Lesson (
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL,
  timeBefore    INTEGER,
  timeAfter     INTEGER,
  createDate    TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%S','now', 'localtime')),
  disciplineId  INTEGER NOT NULL,
  lecturerId    INTEGER NOT NULL,
  streamId      INTEGER,
  FOREIGN KEY (disciplineId)  REFERENCES Discipline(id),
  FOREIGN KEY (lecturerId)    REFERENCES Lecturer(id),
  FOREIGN KEY (streamId)      REFERENCES Stream(id)
);

-- CLASS
CREATE TABLE Class (
  id            INTEGER PRIMARY KEY,
  date          TEXT NOT NULL,
  scheduleId    INTEGER NOT NULL,
  FOREIGN KEY (scheduleId)    REFERENCES Schedule(id)
);

-- LESSON-CLASS
CREATE TABLE Lesson_Class (
  lessonId      INTEGER NOT NULL,
  classId       INTEGER NOT NULL,
  FOREIGN KEY (lessonId)      REFERENCES Lesson(id),
  FOREIGN KEY (classId)       REFERENCES Class(id)
);

-- STUDENT-CLASS
CREATE TABLE Student_Class (
  studentId     INTEGER NOT NULL,
  classId       INTEGER NOT NULL,
  present       INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (studentId)     REFERENCES Student(id),
  FOREIGN KEY (classId)       REFERENCES Class(id)
);

COMMIT;
