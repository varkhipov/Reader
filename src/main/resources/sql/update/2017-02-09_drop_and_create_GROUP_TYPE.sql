COMMIT;
BEGIN;

DROP TABLE IF EXISTS GROUP_TYPE;

CREATE TABLE GROUP_TYPE (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT
);

COMMIT;