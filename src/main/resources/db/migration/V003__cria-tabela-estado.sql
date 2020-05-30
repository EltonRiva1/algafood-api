CREATE TABLE estado (
	id bigint NOT null AUTO_INCREMENT,
	nome varchar(80) NOT null,
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT charset=utf8;

INSERT INTO estado (nome) SELECT DISTINCT nome_estado FROM cidade;

ALTER TABLE cidade ADD COLUMN estado_id bigint NOT null;

UPDATE cidade c SET c.estado_id = (SELECT e.id FROM estado e where e.nome=c.nome_estado);

ALTER TABLE cidade ADD CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado (id);

ALTER TABLE cidade DROP COLUMN nome_estado;

ALTER TABLE cidade CHANGE nome_cidade nome varchar(80) NOT null;