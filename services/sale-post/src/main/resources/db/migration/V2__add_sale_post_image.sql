ALTER TABLE sale_post
    DROP COLUMN image_object_key;

CREATE TABLE sale_post_image
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_post_id BIGINT       NOT NULL,
    object_key   VARCHAR(500) NOT NULL,
    sort_order   INT          NOT NULL,
    created_at   DATETIME     NOT NULL,
    INDEX idx_sale_post_image_sale_post_id (sale_post_id)
);
