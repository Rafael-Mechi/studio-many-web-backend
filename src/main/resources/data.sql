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
    ('aguardando sinal'),
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

-- Dias de trabalho dos profissionais
-- Beatriz Administradora (id 1) - Segunda a Sexta, 09:00 as 18:00
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('MONDAY', '09:00:00', '18:00:00', 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('TUESDAY', '09:00:00', '18:00:00', 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('WEDNESDAY', '09:00:00', '18:00:00', 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('THURSDAY', '09:00:00', '18:00:00', 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('FRIDAY', '09:00:00', '18:00:00', 1);


-- Isabelly Profissional (id 2)
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('MONDAY', '08:00:00', '19:00:00', 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('TUESDAY', '08:00:00', '19:00:00', 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('WEDNESDAY', '08:00:00', '19:00:00', 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('THURSDAY', '08:00:00', '19:00:00', 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('FRIDAY', '08:00:00', '19:00:00', 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id)
VALUES ('SATURDAY', '08:00:00', '14:00:00', 2);


-- INSERCOES PARA TESTAR A DISPONIBILIDADE DO AGENDAMENTO
INSERT INTO usuarios (email, senha, perfil_id, criado_em)
VALUES (
    'cliente@studio.com',
    'hash_cliente',
    3,
    CURRENT_TIMESTAMP
);

INSERT INTO clientes (
    nome,
    telefone,
    documento,
    usuario_id
) VALUES (
    'Cliente Teste',
    '11999999999',
    '12345678900',
    3
);

INSERT INTO bloqueios (
    inicio,
    fim,
    motivo,
    profissional_id
) VALUES (
    '2026-08-25 12:00:00',
    '2026-08-25 14:00:00',
    'Almoço/reunião',
    1
);

INSERT INTO bloqueios (
    inicio,
    fim,
    motivo,
    profissional_id
) VALUES (
    '2026-08-26 10:00:00',
    '2026-08-26 11:30:00',
    'Compromisso pessoal',
    2
);

INSERT INTO agendamentos (
    criado_em,
    preco,
    desconto_porcentagem,
    preco_final,
    criado_por_usuario_id,
    cliente_id,
    pacote_id,
    profissional_id,
    status_agendamento_id
) VALUES (
    '2026-08-20 10:00:00',
    150.00,
    0.00,
    150.00,
    3,
    1,
    2,
    1,
    2
);

INSERT INTO agendamento_itens (
    inicio_atendimento,
    fim_atendimento,
    agendamento_id,
    servico_id,
    profissional_id
) VALUES (
    '2026-08-27 10:00:00',
    '2026-08-27 11:00:00',
    1,
    1,
    1
);

INSERT INTO agendamentos (
    criado_em,
    preco,
    desconto_porcentagem,
    preco_final,
    criado_por_usuario_id,
    cliente_id,
    pacote_id,
    profissional_id,
    status_agendamento_id
) VALUES (
    '2026-08-20 10:00:00',
    150.00,
    0.00,
    150.00,
    3,
    1,
    2,
    2,
    2
);

INSERT INTO agendamento_itens (
    inicio_atendimento,
    fim_atendimento,
    agendamento_id,
    servico_id,
    profissional_id
) VALUES (
    '2026-08-28 14:00:00',
    '2026-08-28 15:00:00',
    2,
    1,
    2
);