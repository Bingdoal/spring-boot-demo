-- TABLE: user
DROP TABLE IF EXISTS "user" ;

CREATE TABLE "user"
(
    "id" bigserial NOT NULL PRIMARY KEY,
    "name" character varying(255) NOT NULL UNIQUE,
    "password" text NOT NULL,
    "email" character varying(255) NOT NULL UNIQUE,
    "creation_time" timestamp with time zone,
    "modification_time" timestamp with time zone
);

---