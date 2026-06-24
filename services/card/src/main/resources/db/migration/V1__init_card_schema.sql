CREATE TABLE sets (
    id               VARCHAR(50)  NOT NULL,
    name             VARCHAR(100) NOT NULL,
    series           VARCHAR(100) NOT NULL,
    printed_total    INT          NOT NULL DEFAULT 0,
    total            INT          NOT NULL DEFAULT 0,
    ptcgo_code       VARCHAR(20),
    release_date     DATE,
    symbol_image_url VARCHAR(500),
    logo_image_url   VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE cards (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    set_id           VARCHAR(50)  NOT NULL,
    number           VARCHAR(20)  NOT NULL,
    name             VARCHAR(200) NOT NULL,
    supertype        VARCHAR(20)  NOT NULL,
    subtype          VARCHAR(50),
    rarity           VARCHAR(50),
    image_small_url  VARCHAR(500),
    image_large_url  VARCHAR(500),
    PRIMARY KEY (id),
    UNIQUE KEY uq_set_number (set_id, number),
    INDEX idx_card_set_id (set_id)
);

CREATE TABLE card_types (
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    card_id BIGINT      NOT NULL,
    type    VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_card_type_card_id (card_id)
);
