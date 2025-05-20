CREATE TABLE tb_weight (
    id bigserial NOT NULL,
    value numeric(5, 1) NOT NULL,
    unit varchar(50) NOT NULL,
    created_at timestamp NOT NULL,
    exercise_id bigint NULL,
    CONSTRAINT tb_weight__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_weight__f_exercise_id__fk FOREIGN KEY (exercise_id) REFERENCES tb_exercise(id)
);