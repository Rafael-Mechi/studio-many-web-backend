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
INSERT INTO servicos (nome, preco, duracao_minutos, ativo) VALUES ('Limpeza de pele', 150.00, 60, TRUE);
INSERT INTO servicos (nome, preco, duracao_minutos, ativo) VALUES ('Unha', 150.00, 60, TRUE);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, 1);
--
--INSERT INTO usuarios (email, senha, perfil_id) VALUES ('cliente.joana@email.com', 'hash456', 3);
--INSERT INTO clientes (nome, telefone, usuario_id) VALUES ('Joana Santos', '11999999999', 2);
--INSERT INTO anamneses (informacao) VALUES ('Ficha Inicial Joana');
--INSERT INTO anamnese_clientes (anamneses_id, clientes_id) VALUES (1, 1);