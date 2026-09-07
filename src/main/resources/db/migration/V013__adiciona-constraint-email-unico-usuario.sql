alter table usuario
    add constraint uk_usuario_email unique (email);