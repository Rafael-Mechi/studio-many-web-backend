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
    ('check-in'),
    ('em atendimento');

INSERT INTO status_pagamentos (estado) VALUES
    ('cancelado'),
    ('pendente'),
    ('pago');

INSERT INTO tipo_pagamentos (tipo) VALUES ('sinal');

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
    'https://servicos-studio-essencia.s3.us-east-1.amazonaws.com/limpeza-de-pele-profunda-voce-conhece-todos-os-seus-beneficios-danielle-sales.jpg',
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
    'https://servicos-studio-essencia.s3.us-east-1.amazonaws.com/images.jfif ',
    60,
    150.00,
    50.00,
    TRUE,
    CURRENT_TIMESTAMP,
    2
);


INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, 1);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Limpeza de Pele Avulsa', 1, 150.00, 30, TRUE, 1);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Remoção de cravos avulsa', 1, 700.00, 30, TRUE, 2);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Kit remoção de cravos 5x', 5, 700.00, 30, TRUE, 2);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Kit remoção de cravos 10x', 10, 1500.00, 30, TRUE, 2);
-- Usuarios e Profissionais
INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('noa@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 1, CURRENT_TIMESTAMP);
INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('noa2@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 2, CURRENT_TIMESTAMP);

INSERT INTO profissionais (nome, telefone, documento, almoco_inicio, almoco_fim, usuario_id) VALUES ('Beatriz Administradora', '11988887777', '11111111111', '12:00:00', '13:00:00', 1);
INSERT INTO profissionais (nome, telefone, usuario_id) VALUES ('Isabelly Profissional', '11977776666', 2);

-- Servicos dos Profissionais
-- Ambos profissionais fazem Limpeza de pele (servico 1)
-- Apenas o profissional Isabelly (id 2) também faz Remoção de cravos (servico 2)
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (1, 1);
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (2, 1);
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (1, 2);
INSERT INTO servicos_profissionais (servicos_id, profissionais_id) VALUES (2, 2);

-- Dias de trabalho dos profissionais (com servico_id para a tela de configurações)
-- Beatriz Administradora (id 1) - Limpeza de pele (id 1) de Segunda a Sexta 09:00 as 18:00;
-- Remoção de cravos (id 2) de Terça a Quinta 09:00 as 18:00
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('MONDAY', '09:00:00', '18:00:00', 1, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('TUESDAY', '09:00:00', '18:00:00', 1, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('WEDNESDAY', '09:00:00', '18:00:00', 1, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('THURSDAY', '09:00:00', '18:00:00', 1, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('FRIDAY', '09:00:00', '18:00:00', 1, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('TUESDAY', '09:00:00', '18:00:00', 1, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('WEDNESDAY', '09:00:00', '18:00:00', 1, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('THURSDAY', '09:00:00', '18:00:00', 1, 2);


-- Isabelly Profissional (id 2) - Limpeza de pele (id 1) e Remoção de cravos (id 2)
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('MONDAY', '08:00:00', '19:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('MONDAY', '08:00:00', '19:00:00', 2, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('TUESDAY', '08:00:00', '19:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('TUESDAY', '08:00:00', '19:00:00', 2, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('WEDNESDAY', '08:00:00', '19:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('WEDNESDAY', '08:00:00', '19:00:00', 2, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('THURSDAY', '08:00:00', '19:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('THURSDAY', '08:00:00', '19:00:00', 2, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('FRIDAY', '08:00:00', '19:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('FRIDAY', '08:00:00', '19:00:00', 2, 2);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('SATURDAY', '08:00:00', '14:00:00', 2, 1);
INSERT INTO dias_de_trabalho (dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id)
VALUES ('SATURDAY', '08:00:00', '14:00:00', 2, 2);


-- INSERCOES PARA TESTAR A DISPONIBILIDADE DO AGENDAMENTO
INSERT INTO usuarios (email, senha, perfil_id, criado_em)
VALUES (
    'noa3@gmail.com',
    '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS',
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

-- Datas relativas a hoje (H2): os seeds nunca ficam obsoletos.
-- Agendamentos/itens usam dias relativos + horários fixos para continuar legíveis.
INSERT INTO bloqueios (
    inicio,
    fim,
    motivo,
    profissional_id
) VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 2, CURRENT_DATE) AS VARCHAR), ' 12:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 2, CURRENT_DATE) AS VARCHAR), ' 14:00:00') AS TIMESTAMP),
    'Almoço/reunião',
    1
);

INSERT INTO bloqueios (
    inicio,
    fim,
    motivo,
    profissional_id
) VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 3, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 3, CURRENT_DATE) AS VARCHAR), ' 11:30:00') AS TIMESTAMP),
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
    DATEADD('DAY', -8, CURRENT_TIMESTAMP),
    150.00,
    0.00,
    150.00,
    3,
    1,
    2,
    1,
    9
);

INSERT INTO agendamento_itens (
    inicio_atendimento,
    fim_atendimento,
    agendamento_id,
    servico_id,
    profissional_id
) VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 1, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 1, CURRENT_DATE) AS VARCHAR), ' 11:00:00') AS TIMESTAMP),
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
    DATEADD('DAY', -8, CURRENT_TIMESTAMP),
    150.00,
    0.00,
    150.00,
    3,
    1,
    2,
    2,
    3
);

INSERT INTO agendamento_itens (
    inicio_atendimento,
    fim_atendimento,
    agendamento_id,
    servico_id,
    profissional_id
) VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 2, CURRENT_DATE) AS VARCHAR), ' 14:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 2, CURRENT_DATE) AS VARCHAR), ' 15:00:00') AS TIMESTAMP),
    2,
    1,
    2
);

-- Segundo cliente, pra testar isolamento entre clientes também
INSERT INTO usuarios (email, senha, perfil_id, criado_em)
VALUES ('maria.cliente@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 3, CURRENT_TIMESTAMP);

INSERT INTO clientes (nome, telefone, documento, usuario_id)
VALUES ('Maria Cliente', '11988885555', '98765432100', 4);

-- Agendamento 3: Cliente Teste com Isabelly, CONCLUIDO, mais antigo
INSERT INTO agendamentos (criado_em, preco, desconto_porcentagem, preco_final, criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id)
VALUES (DATEADD('DAY', -18, CURRENT_TIMESTAMP), 150.00, 0.00, 150.00, 3, 1, 3, 2, 9);

INSERT INTO agendamento_itens (inicio_atendimento, fim_atendimento, agendamento_id, servico_id, profissional_id)
VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', -13, CURRENT_DATE) AS VARCHAR), ' 09:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', -13, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    3, 2, 2);

-- Agendamento 4: Cliente Teste com Isabelly, CANCELADO, mais recente que o concluído
INSERT INTO agendamentos (criado_em, preco, desconto_porcentagem, preco_final, criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id)
VALUES (DATEADD('DAY', -3, CURRENT_TIMESTAMP), 150.00, 0.00, 150.00, 3, 1, 2, 2, 5);

INSERT INTO agendamento_itens (inicio_atendimento, fim_atendimento, agendamento_id, servico_id, profissional_id)
VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 1, CURRENT_DATE) AS VARCHAR), ' 09:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 1, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    4, 1, 2);

-- Agendamento 5: Maria Cliente com Beatriz, AGENDADO (futuro, não deve contar como visita)
INSERT INTO agendamentos (criado_em, preco, desconto_porcentagem, preco_final, criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id)
VALUES (DATEADD('DAY', -1, CURRENT_TIMESTAMP), 150.00, 0.00, 150.00, 3, 2, 2, 1, 2);

INSERT INTO agendamento_itens (inicio_atendimento, fim_atendimento, agendamento_id, servico_id, profissional_id)
VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', 3, CURRENT_DATE) AS VARCHAR), ' 09:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', 3, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    5, 1, 1);

-- Agendamento 6: Maria Cliente, cabeçalho diz Beatriz (profissional_id=1),
-- mas quem realmente atendeu (no item) foi a Isabelly (profissional_id=2). CONCLUIDO.
INSERT INTO agendamentos (criado_em, preco, desconto_porcentagem, preco_final, criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id)
VALUES (DATEADD('DAY', -23, CURRENT_TIMESTAMP), 150.00, 0.00, 150.00, 3, 2, 3, 1, 9);

INSERT INTO agendamento_itens (inicio_atendimento, fim_atendimento, agendamento_id, servico_id, profissional_id)
VALUES (
    CAST(CONCAT(CAST(DATEADD('DAY', -18, CURRENT_DATE) AS VARCHAR), ' 09:00:00') AS TIMESTAMP),
    CAST(CONCAT(CAST(DATEADD('DAY', -18, CURRENT_DATE) AS VARCHAR), ' 10:00:00') AS TIMESTAMP),
    6, 2, 2);