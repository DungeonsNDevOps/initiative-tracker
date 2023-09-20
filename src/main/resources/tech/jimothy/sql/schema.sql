CREATE TABLE campaigns (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE entities (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    bonus INTEGER NOT NULL,
    type TEXT NOT NULL
);

CREATE TABLE effects (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    desc TEXT NOT NULL,
    duration INTEGER NOT NULL
);