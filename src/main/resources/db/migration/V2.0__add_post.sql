CREATE TABLE "post"
(
    "id" bigserial NOT NULL PRIMARY KEY,
    "content" text NOT NULL,
    "author_id" bigint NOT NULL,
    "creation_time" timestamp with time zone,
    "modification_time" timestamp with time zone
);

ALTER TABLE "post" ADD CONSTRAINT "post_author" FOREIGN KEY ("author_id") REFERENCES "user" ("id");