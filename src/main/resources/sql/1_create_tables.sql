BEGIN;

-- SCHEDULE
CREATE TABLE Schedule(
  id            INTEGER PRIMARY KEY,
  begin         TEXT NOT NULL,
  end           TEXT NOT NULL
);

-- DISCIPLINE
CREATE TABLE Discipline(
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL
);

-- LECTURER
CREATE TABLE Lecturer(
  id            INTEGER PRIMARY KEY,
  uid           TEXT UNIQUE,
  name          TEXT NOT NULL,
  surame        TEXT NOT NULL,
  patronymic    TEXT,
  phone         TEXT,
  email         TEXT,
  notes         TEXT,
  photo         BLOB
);

-- GROUP (used brackets because GROUP is reserved keyword)
CREATE TABLE [Group](
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL
);

-- STUDENT
CREATE TABLE Student(
  id            INTEGER PRIMARY KEY,
  uid           TEXT NOT NULL UNIQUE,
  name          TEXT NOT NULL,
  surame        TEXT NOT NULL,
  patronymic    TEXT,
  phone         TEXT,
  email         TEXT,
  notes         TEXT,
  photo         BLOB,
  groupId       INTEGER,
  FOREIGN KEY (groupId)       REFERENCES [Group](id)
);

-- LECTURE
CREATE TABLE Lecture(
  id            INTEGER PRIMARY KEY,
  name          TEXT NOT NULL,
  timeBefore    INTEGER,
  timeAfter     INTEGER,
  dateTime      TEXT,
  scheduleId    INTEGER NOT NULL,
  disciplineId  INTEGER NOT NULL,
  lecturerId    INTEGER NOT NULL,
  groupId       INTEGER,
  FOREIGN KEY (scheduleId)    REFERENCES Schedule(id),
  FOREIGN KEY (disciplineId)  REFERENCES Discipline(id),
  FOREIGN KEY (lecturerId)    REFERENCES Lecturer(id),
  FOREIGN KEY (groupId)       REFERENCES [Group](id)
);

-- LECTURE-STUDENT
CREATE TABLE Lecture_Student(
  lectureId     INTEGER NOT NULL,
  studentId     INTEGER NOT NULL,
  FOREIGN KEY (lectureId)     REFERENCES Lecture(id),
  FOREIGN KEY (studentId)     REFERENCES Student(id)
);

COMMIT;
