CREATE TABLE tb_exercise (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    description varchar(255) NULL,
    additional varchar(150) NULL,
    sets int NOT NULL,
    weight real NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NULL,
    workout_id bigint NOT NULL,
    CONSTRAINT tb_exercise__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_exercise__f_workout_id__fk FOREIGN KEY (workout_id) REFERENCES tb_workout(id)
);