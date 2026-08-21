INSERT INTO perfis (perfil) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_PROFISSIONAL'),
    ('ROLE_CLIENTE');

INSERT INTO status_agendamentos (estado) VALUES
    ('solicitar confirmacao agendamento'),
    ('agendado'),
    ('confirmado'),
    ('solicitar cancelamento'),
    ('cancelado'),
    ('solicitar reagendamento'),
    ('reagendado'),
    ('recusado'),
    ('concluido'),
    ('faltou'),
    ('em atendimento');

INSERT INTO status_pagamentos (estado) VALUES
    ('Cancelado'),
    ('Pendente'),
    ('Pago');

--INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('ana.estetica@email.com', 'hash123', 2, NOW());
--INSERT INTO profissionais (nome, usuario_id) VALUES ('Ana Silva', 1);
--
--
--INSERT INTO usuarios (email, senha, perfil_id) VALUES ('cliente.joana@email.com', 'hash456', 3);
--INSERT INTO clientes (nome, telefone, usuario_id) VALUES ('Joana Santos', '11999999999', 2);
--INSERT INTO anamneses (informacao) VALUES ('Ficha Inicial Joana');
--INSERT INTO anamnese_clientes (anamneses_id, clientes_id) VALUES (1, 1);

INSERT INTO categoria_servicos (categoria) VALUES('Corporal');
INSERT INTO categoria_servicos (categoria) VALUES('Facial');

INSERT INTO servicos (
    nome,
    descricao,
    foto_url,
    duracao_minutos,
    preco,
    sinal_valor,
    ativo,
    criado_em,
    fk_categoria_servico
) VALUES (
    'Limpeza de pele',
    'Procedimento para remover impurezas, células mortas, cravos e miliuns da superfície do rosto.',
    'https://link_bucket',
    60,
    150.00,
    50.00,
    TRUE,
    CURRENT_TIMESTAMP,
    1
);

INSERT INTO servicos (
    nome,
    descricao,
    foto_url,
    duracao_minutos,
    preco,
    sinal_valor,
    ativo,
    criado_em,
    fk_categoria_servico
) VALUES (
    'Remoção de cravos',
    'Procedimento para remoção de cravos.',
    'https://link_bucket',
    60,
    150.00,
    50.00,
    TRUE,
    CURRENT_TIMESTAMP,
    2
);


INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, 1);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Limpeza de Pele Avulsa', 1, 150.00, 30, TRUE, 1);

-- Usuarios e Profissionais
INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('admin@studio.com', 'hash_admin', 1, CURRENT_TIMESTAMP);
INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('profissional@studio.com', 'hash_profissional', 2, CURRENT_TIMESTAMP);

INSERT INTO profissionais (nome, telefone, usuario_id) VALUES ('Beatriz Administradora', '11988887777', 1);
INSERT INTO profissionais (nome, telefone, usuario_id) VALUES ('Isabelly Profissional', '11977776666', 2);

-- Servicos dos Profissionais
-- Ambos profissionais fazem Limpeza de pele (servico 1)
-- Apenas o profissional Isabelly (id 2) também faz Remoção de cravos (servico 2)
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (1, 1);
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (1, 2);
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (2, 2);