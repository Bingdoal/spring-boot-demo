CREATE TABLE "order" (
    "id" bigserial NOT NULL PRIMARY KEY,
    "user_id" bigint NOT NULL,
    "item" character varying(255) NOT NULL,
    "status" int NOT NULL,
    "creation_time" timestamp with time zone,
    "modification_time" timestamp with time zone
);

COMMENT ON COLUMN "order"."status" is '0:訂單取消 1:訂單成功 2:訂單處理中 3:訂單處理完成';