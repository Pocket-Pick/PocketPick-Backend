ALTER TABLE sale_post
    DROP COLUMN card_id,
    DROP COLUMN card_condition;

CREATE TABLE sale_post_item
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_post_id   BIGINT      NOT NULL,
    card_id        BIGINT      NOT NULL,
    card_condition VARCHAR(20) NOT NULL,
    quantity       INT         NOT NULL DEFAULT 1,
    INDEX idx_sale_post_item_sale_post_id (sale_post_id)
);
