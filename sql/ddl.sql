CREATE TABLE wrong_list
(
    wr_pkid     INT PRIMARY KEY NOT NULL,
    fo_pkid     INT             NOT NULL,
    create_time TEXT            NOT NULL,
    modify_time TEXT            NOT NULL
);

CREATE TABLE folder
(
    fo_pkid     INT PRIMARY KEY NOT NULL,
    fo_name     TEXT            NOT NULL,
    create_time TEXT            NOT NULL,
    modify_time TEXT            NOT NULL
);

CREATE TABLE "card"
(
    ca_pkid            INT           not null
        primary key,
    fo_pkid            INT           not null,
    grammatical_person TEXT,
    verb               TEXT,
    conjugation        TEXT          not null,
    eg_sentence        TEXT,
    review_time        TEXT,
    if_done            INT default 0 not null,
    create_time        TEXT          not null,
    modify_time        TEXT          not null,
    hit_times          INT default 0,
    last_review_time   TEXT
);