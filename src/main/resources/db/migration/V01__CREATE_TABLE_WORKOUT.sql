CREATE TABLE tb_workout (
    id bigserial NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    is_in_use boolean NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_workout_f_id_pk PRIMARY KEY (id)
);