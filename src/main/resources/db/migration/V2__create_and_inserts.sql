CREATE SEQUENCE SQ_TAB_GEOMOTTU_FILIAL START WITH 1 INCREMENT BY 1;

-- Cria a tabela de filial
CREATE TABLE TAB_GEOMOTTU_FILIAL (
    id_filial NUMBER(19) NOT NULL PRIMARY KEY,
    nm_filial VARCHAR2(255) NOT NULL,
    pais_filial VARCHAR2(255) NOT NULL,
    nm_telefone VARCHAR2(20),
    ds_email VARCHAR2(50),
    estado VARCHAR2(255),
    sigla_estado VARCHAR2(2),
    cidade VARCHAR2(255),
    rua VARCHAR2(255)
);

INSERT INTO TAB_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_TAB_GEOMOTTU_FILIAL.nextval, 'Mottu - Sede São Paulo', 'BRASIL', '+5511999990001', 'contato.sp@mottu.com.br', 'São Paulo', 'SP', 'São Paulo', 'Av. Paulista, 1000');

INSERT INTO TAB_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_TAB_GEOMOTTU_FILIAL.nextval, 'Mottu - Hub Rio de Janeiro', 'BRASIL', '+5521999990002', 'contato.rj@mottu.com.br', 'Rio de Janeiro', 'RJ', 'Rio de Janeiro', 'Av. Atlântica, 2000');

INSERT INTO TAB_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_TAB_GEOMOTTU_FILIAL.nextval, 'Mottu - CDMX Polanco', 'MEXICO', '+5255999990003', 'contacto.cdmx@mottu.mx', 'Ciudad de México', 'CM', 'Ciudad de México', 'Av. Presidente Masaryk, 300');

INSERT INTO TAB_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_TAB_GEOMOTTU_FILIAL.nextval, 'Mottu - Guadalajara Centro', 'MEXICO', '+5233999990004', 'contacto.gdl@mottu.mx', 'Jalisco', 'JA', 'Guadalajara', 'Av. Juárez, 400');

INSERT INTO TAB_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_TAB_GEOMOTTU_FILIAL.nextval, 'Mottu - Monterrey', 'MEXICO', '+5281999990005', 'contacto.mty@mottu.mx', 'Nuevo León', 'NL', 'Monterrey', 'Calz. del Valle, 500');

CREATE SEQUENCE SQ_TAB_GEOMOTTU_USUARIO START WITH 1 INCREMENT BY 1;

CREATE TABLE TAB_GEOMOTTU_USUARIO (
    id_usuario NUMBER(19) NOT NULL PRIMARY KEY,
    nm_usuario VARCHAR2(25) NOT NULL UNIQUE,
    ds_senha VARCHAR2(100) NOT NULL,
    tp_perfil NUMBER(10) NOT NULL,
    id_filial NUMBER(19),
    CONSTRAINT fk_usuario_filial FOREIGN KEY (id_filial) REFERENCES TAB_GEOMOTTU_FILIAL(id_filial)
);

INSERT INTO TAB_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_TAB_GEOMOTTU_USUARIO.nextval, 'admin', '@Admin123', 1, 1);
INSERT INTO TAB_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_TAB_GEOMOTTU_USUARIO.nextval, 'joao.silva', '$User5432', 2, 1);
INSERT INTO TAB_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_TAB_GEOMOTTU_USUARIO.nextval, 'maria.souza', '$User1489', 2, 2);
INSERT INTO TAB_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_TAB_GEOMOTTU_USUARIO.nextval, 'carlos.gomez', '$User5892', 2, 3);
INSERT INTO TAB_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial) VALUES (SQ_TAB_GEOMOTTU_USUARIO.nextval, 'ana.hernandez', '$User1234', 2, 3);

CREATE SEQUENCE SQ_TAB_GEOMOTTU_PATIO START WITH 1 INCREMENT BY 1;

CREATE TABLE TAB_GEOMOTTU_PATIO (
    id_patio NUMBER(19) NOT NULL PRIMARY KEY,
    nm_patio VARCHAR2(50),
    nr_capacidade NUMBER(10) NOT NULL,
    id_filial NUMBER(19),
    CONSTRAINT fk_patio_filial FOREIGN KEY (id_filial) REFERENCES TAB_GEOMOTTU_FILIAL(id_filial)
);

INSERT INTO TAB_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_TAB_GEOMOTTU_PATIO.nextval, 'Pátio Central SP', 250, 1);
INSERT INTO TAB_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_TAB_GEOMOTTU_PATIO.nextval, 'Pátio Zona Sul SP', 150, 1);
INSERT INTO TAB_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_TAB_GEOMOTTU_PATIO.nextval, 'Pátio Copacabana RJ', 180, 2);
INSERT INTO TAB_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_TAB_GEOMOTTU_PATIO.nextval, 'Pátio Polanco CDMX', 300, 3);
INSERT INTO TAB_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_TAB_GEOMOTTU_PATIO.nextval, 'Pátio Jalisco GDL', 220, 4);

CREATE SEQUENCE SQ_TAB_GEOMOTTU_MOTO START WITH 1 INCREMENT BY 1;

CREATE TABLE TAB_GEOMOTTU_MOTO (
    id_moto NUMBER(19) NOT NULL PRIMARY KEY,
    nr_placa VARCHAR2(8) UNIQUE,
    nr_chassi VARCHAR2(50) UNIQUE,
    tp_modelo VARCHAR2(255) NOT NULL,
    st_operacional VARCHAR2(255) NOT NULL,
    id_patio NUMBER(19),
    CONSTRAINT fk_moto_patio FOREIGN KEY (id_patio) REFERENCES TAB_GEOMOTTU_PATIO(id_patio)
);

INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'BRA1A11', 'CHASSIBR001', 'MOTTUPOP', 'LIVRE', 1);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'BRA2B22', 'CHASSIBR002', 'MOTTUPOP', 'ALUGADA', 1);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'BRA3C33', 'CHASSIBR003', 'MOTTUSPORT', 'MANUTENCAO', 1);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'BRA4D44', 'CHASSIBR004', 'MOTTUE', 'LIVRE', 2);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'BRA5E55', 'CHASSIBR005', 'MOTTUSPORT', 'LIVRE', 3);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'MEX1X11', 'CHASSIMX001', 'MOTTUPOP', 'ALUGADA', 4);
INSERT INTO TAB_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_TAB_GEOMOTTU_MOTO.nextval, 'MEX2Y22', 'CHASSIMX002', 'MOTTUE', 'LIVRE', 4);