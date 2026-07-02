CREATE TABLE sale_post
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT       NOT NULL,
    card_id          BIGINT       NOT NULL,
    title            VARCHAR(100) NOT NULL,
    description      TEXT,
    price            INT          NOT NULL,
    card_condition   VARCHAR(20)  NOT NULL,
    status           VARCHAR(20)  NOT NULL DEFAULT 'ON_SALE',
    image_object_key VARCHAR(500),
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL
);
