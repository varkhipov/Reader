COMMIT;
BEGIN;

-- LECTURER
CREATE TABLE LECTURER (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  uid               INTEGER,
  first_name        TEXT,
  last_name         TEXT,
  patronymic        TEXT,
  phone             TEXT,
  email             TEXT,
  image             BLOB
);

-- SCHEDULE_VERSION
CREATE TABLE SCHEDULE_VERSION (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  start_date        TEXT,
  end_date          TEXT
);

-- SCHEDULE
CREATE TABLE SCHEDULE (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  begin             TEXT,
  end               TEXT,
  number            INTEGER,
  version_id        INTEGER,
  FOREIGN KEY (version_id)     REFERENCES SCHEDULE_VERSION(id)
);

-- DEPARTMENT
CREATE TABLE DEPARTMENT (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  abbreviation      TEXT
);

-- STREAM
CREATE TABLE STREAM (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  image             BLOB
);

-- GROUP_TYPE
CREATE TABLE GROUP_TYPE (
  id INTEGER,
  name TEXT
);

-- GROUP (used brackets because GROUP is reserved keyword)
CREATE TABLE [GROUP] (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  department_id     INTEGER,
  type_id           INTEGER,
  image             BLOB,
  active            INTEGER,
  expiration_date   TEXT,
  FOREIGN KEY (department_id)  REFERENCES DEPARTMENT(id),
  FOREIGN KEY (type_id)        REFERENCES GROUP_TYPE(id)
);

-- STUDENT
CREATE TABLE STUDENT (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  uid               TEXT,
  parsed_uid        TEXT,
  first_name        TEXT,
  last_name         TEXT,
  patronymic        TEXT,
  phone             TEXT,
  email             TEXT,
  image             BLOB
);

-- COURSE
CREATE TABLE COURSE (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  description       TEXT,
  create_date       TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%S','now', 'localtime')),
  lecturer_id       INTEGER,
  stream_id         INTEGER,
  active            INTEGER,
  expiration_date   TEXT,
  FOREIGN KEY (lecturer_id)    REFERENCES LECTURER(id),
  FOREIGN KEY (stream_id)      REFERENCES [GROUP](id)
);

-- LESSON_TYPE
CREATE TABLE LESSON_TYPE (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT
);

-- LESSON
CREATE TABLE LESSON (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  description       TEXT,
  course_id         INTEGER,
  create_date       TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%S','now', 'localtime')),
  type_id           INTEGER,
  group_id          INTEGER,
  FOREIGN KEY (course_id)      REFERENCES COURSE(id),
  FOREIGN KEY (type_id)        REFERENCES LESSON_TYPE(id),
  FOREIGN KEY (group_id)       REFERENCES [GROUP](id)
);

-- CLASS
CREATE TABLE CLASS (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  date              TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%S','now', 'localtime')),
  schedule_id       INTEGER,
  session_start     TEXT,
  session_end       TEXT,
  FOREIGN KEY (schedule_id)    REFERENCES SCHEDULE(id)
);

-- NOTE_TYPE
CREATE TABLE NOTE_TYPE (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT
);

-- NOTE
CREATE TABLE NOTE (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  lecturer_id       INTEGER,
  type_id           INTEGER,
  entity_id         INTEGER,
  description       TEXT,
  create_date       TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%S','now', 'localtime')),
  FOREIGN KEY (lecturer_id)    REFERENCES LECTURER(id),
  FOREIGN KEY (type_id)        REFERENCES NOTE_TYPE(id)
);

--
-- MANY-TO-MANY --
--

-- STUDENT-GROUP
CREATE TABLE STUDENT_GROUP (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  student_id        INTEGER,
  group_id          INTEGER,
  praepostor        INTEGER,
  FOREIGN KEY (student_id)     REFERENCES STUDENT(id),
  FOREIGN KEY (group_id)       REFERENCES [GROUP](id)
);

-- STREAM-GROUP
CREATE TABLE STREAM_GROUP (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  stream_id         INTEGER,
  group_id          INTEGER,
  FOREIGN KEY (stream_id)      REFERENCES STREAM(id),
  FOREIGN KEY (group_id)       REFERENCES [GROUP](id)
);

-- STUDENT-CLASS
CREATE TABLE STUDENT_CLASS (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  student_id        INTEGER,
  class_id          INTEGER,
  registered        INTEGER DEFAULT 0,
  registration_time TEXT,
  registration_type TEXT,
  mark              TEXT,
  FOREIGN KEY (student_id)     REFERENCES STUDENT(id),
  FOREIGN KEY (class_id)       REFERENCES CLASS(id)
);

-- LESSON-CLASS
CREATE TABLE LESSON_CLASS (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  lesson_id         INTEGER,
  class_id          INTEGER,
  FOREIGN KEY (lesson_id)      REFERENCES LESSON(id),
  FOREIGN KEY (class_id)       REFERENCES CLASS(id)
);

COMMIT;
