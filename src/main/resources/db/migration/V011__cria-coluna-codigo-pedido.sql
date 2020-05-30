ALTER TABLE pedido ADD codigo varchar(36) NOT null AFTER id;

UPDATE pedido SET codigo = uuid();

ALTER TABLE pedido ADD CONSTRAINT uk_pedido_codigo UNIQUE (codigo);