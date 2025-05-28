CREATE TABLE tb_workout (
    id bigserial NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_workout__f_id__pk PRIMARY KEY (id)
);