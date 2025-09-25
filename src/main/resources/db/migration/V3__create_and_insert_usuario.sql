CREATE SEQUENCE SQ_T_GEOMOTTU_USUARIO START WITH 1 INCREMENT BY 1;

CREATE TABLE T_GEOMOTTU_USUARIO (
    id_usuario NUMBER(19) NOT NULL PRIMARY KEY,
    nm_usuario VARCHAR2(25) NOT NULL UNIQUE,
    ds_senha VARCHAR2(100) NOT NULL,
    tp_perfil NUMBER(10) NOT NULL,
    id_filial NUMBER(19),
    CONSTRAINT fk_usuario_filial FOREIGN KEY (id_filial) REFERENCES T_GEOMOTTU_FILIAL(id_filial)
);

INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'admin', '@Admin123', 1, 1);
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'joao.silva', '$User5432', 2, 1);
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'maria.souza', '$User1489', 2, 2);
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'carlos.gomez', '$User5892', 2, 3);
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'ana.hernandez', '$User1234', 2, 3);