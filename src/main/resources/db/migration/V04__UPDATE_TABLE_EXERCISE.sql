ALTER TABLE tb_exercise ADD weight_id bigint;
ALTER TABLE tb_exercise ADD CONSTRAINT tb_exercise__f_weight_id__fk FOREIGN KEY (weight_id) REFERENCES tb_weight(id);