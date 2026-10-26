-- Seed compativel com MySQL (idempotente).
-- Carregado quando SPRING_SQL_INIT_DATA_LOCATIONS=classpath:data-mysql.sql
-- Usa IDs explicitos + INSERT IGNORE para poder rodar em todo boot sem duplicar linhas.
-- O arquivo data.sql original permanece para o H2 (usa DATEADD).

INSERT IGNORE INTO perfis (id, perfil) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_PROFISSIONAL'),
    (3, 'ROLE_CLIENTE');

INSERT IGNORE INTO status_agendamentos (id, estado) VALUES
    (1, 'solicitar confirmacao agendamento'),
    (2, 'agendado'),
    (3, 'confirmado'),
    (4, 'solicitar cancelamento'),
    (5, 'cancelado'),
    (6, 'solicitar reagendamento'),
    (7, 'reagendado'),
    (8, 'recusado'),
    (9, 'concluido'),
    (10, 'faltou'),
    (11, 'aguardando sinal'),
    (12, 'check-in'),
    (13, 'em atendimento');

INSERT IGNORE INTO status_pagamentos (id, estado) VALUES
    (1, 'cancelado'),
    (2, 'pendente'),
    (3, 'pago');

INSERT IGNORE INTO tipo_pagamentos (id, tipo) VALUES (1, 'sinal');

INSERT IGNORE INTO categoria_servicos (id, categoria) VALUES
    (1, 'Corporal'),
    (2, 'Facial');

INSERT IGNORE INTO servicos (
    id,
    nome,
    descricao,
    foto_url,
    duracao_minutos,
    preco,
    sinal_valor,
    ativo,
    criado_em,
    fk_categoria_servico
) VALUES
    (
        1,
        'Limpeza de pele',
        'Procedimento para remover impurezas, células mortas, cravos e miliuns da superfície do rosto.',
        'https://bucket-studio-teste-essencia-comprovante.s3.us-east-1.amazonaws.com/servicos/limpeza-de-pele.jpg',
        60,
        150.00,
        50.00,
        1,
        CURRENT_TIMESTAMP,
        1
    ),
    (
        2,
        'Remoção de cravos',
        'Procedimento para remoção de cravos.',
        'https://bucket-studio-teste-essencia-comprovante.s3.us-east-1.amazonaws.com/servicos/remocao-de-cravos.jfif',
        60,
        150.00,
        50.00,
        1,
        CURRENT_TIMESTAMP,
        2
    );

INSERT IGNORE INTO pacotes (id, nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES
    (1, 'Combo Verão 5x Limpeza', 5, 600.00, 90, 1, 1),
    (2, 'Limpeza de Pele Avulsa', 1, 150.00, 30, 1, 1),
    (3, 'Remoção de cravos avulsa', 1, 700.00, 30, 1, 2),
    (4, 'Kit remoção de cravos 5x', 5, 700.00, 30, 1, 2),
    (5, 'Kit remoção de cravos 10x', 10, 1500.00, 30, 1, 2);

-- Usuarios e Profissionais
INSERT IGNORE INTO usuarios (id, email, senha, perfil_id, criado_em, ativo) VALUES
    (1, 'noa@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 1, CURRENT_TIMESTAMP, 1),
    (2, 'noa2@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 2, CURRENT_TIMESTAMP, 1),
    (3, 'noa3@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 3, CURRENT_TIMESTAMP, 1),
    (4, 'maria.cliente@gmail.com', '$2b$10$6hiVYtvxrE2A4WEnY1aBkO3bdjA0vftjHYCEg8v93GbfVoNjiutjS', 3, CURRENT_TIMESTAMP, 1);

INSERT IGNORE INTO profissionais (id, nome, telefone, documento, almoco_inicio, almoco_fim, usuario_id) VALUES
    (1, 'Beatriz Administradora', '11988887777', '11111111111', '12:00:00', '13:00:00', 1),
    (2, 'Isabelly Profissional', '11977776666', NULL, NULL, NULL, 2);

-- Servicos dos profissionais (ambos fazem os dois servicos)
INSERT IGNORE INTO servicos_profissionais (id, servicos_id, profissionais_id) VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 1, 2),
    (4, 2, 2);

-- Dias de trabalho (com servico_id para a tela de configuracoes)
INSERT IGNORE INTO dias_de_trabalho (id, dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id) VALUES
    -- Beatriz (id 1) - Limpeza de pele (id 1): segunda a sexta
    (1, 'MONDAY', '09:00:00', '18:00:00', 1, 1),
    (2, 'TUESDAY', '09:00:00', '18:00:00', 1, 1),
    (3, 'WEDNESDAY', '09:00:00', '18:00:00', 1, 1),
    (4, 'THURSDAY', '09:00:00', '18:00:00', 1, 1),
    (5, 'FRIDAY', '09:00:00', '18:00:00', 1, 1),
    -- Beatriz (id 1) - Remocao de cravos (id 2): terca a quinta
    (6, 'TUESDAY', '09:00:00', '18:00:00', 1, 2),
    (7, 'WEDNESDAY', '09:00:00', '18:00:00', 1, 2),
    (8, 'THURSDAY', '09:00:00', '18:00:00', 1, 2),
    -- Isabelly (id 2) - Limpeza de pele (id 1)
    (9, 'MONDAY', '08:00:00', '19:00:00', 2, 1),
    (11, 'TUESDAY', '08:00:00', '19:00:00', 2, 1),
    (13, 'WEDNESDAY', '08:00:00', '19:00:00', 2, 1),
    (15, 'THURSDAY', '08:00:00', '19:00:00', 2, 1),
    (17, 'FRIDAY', '08:00:00', '19:00:00', 2, 1),
    (19, 'SATURDAY', '08:00:00', '14:00:00', 2, 1),
    -- Isabelly (id 2) - Remocao de cravos (id 2)
    (10, 'MONDAY', '08:00:00', '19:00:00', 2, 2),
    (12, 'TUESDAY', '08:00:00', '19:00:00', 2, 2),
    (14, 'WEDNESDAY', '08:00:00', '19:00:00', 2, 2),
    (16, 'THURSDAY', '08:00:00', '19:00:00', 2, 2),
    (18, 'FRIDAY', '08:00:00', '19:00:00', 2, 2),
    (20, 'SATURDAY', '08:00:00', '14:00:00', 2, 2);

-- Clientes de teste
INSERT IGNORE INTO clientes (id, nome, telefone, documento, usuario_id) VALUES
    (1, 'Cliente Teste', '11999999999', '12345678900', 3),
    (2, 'Maria Cliente', '11988885555', '98765432100', 4);

-- Bloqueios de agenda (datas relativas a hoje para o seed nunca ficar obsoleto)
INSERT IGNORE INTO bloqueios (id, inicio, fim, motivo, profissional_id) VALUES
    (1, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '12:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '14:00:00'), 'Almoço/reunião', 1),
    (2, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '11:30:00'), 'Compromisso pessoal', 2);

-- Agendamentos de teste
INSERT IGNORE INTO agendamentos (id, criado_em, preco, desconto_porcentagem, preco_final, criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id) VALUES
    (1, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -8 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 1, 9),
    (2, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -8 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 2, 3),
    (3, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -18 DAY), 150.00, 0.00, 150.00, 3, 1, 3, 2, 9),
    (4, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -3 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 2, 5),
    (5, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -1 DAY), 150.00, 0.00, 150.00, 3, 2, 2, 1, 2),
    (6, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -23 DAY), 150.00, 0.00, 150.00, 3, 2, 3, 1, 9),
    (7, '2026-10-11 09:00:00', 150.00, 0.00, 150.00, 3, 2, 2, 2, 2);

INSERT IGNORE INTO agendamento_itens (id, inicio_atendimento, fim_atendimento, agendamento_id, servico_id, profissional_id) VALUES
    (1, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '11:00:00'), 1, 1, 1),
    (2, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '14:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '15:00:00'), 2, 1, 2),
    (3, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -13 DAY), '09:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -13 DAY), '10:00:00'), 3, 2, 2),
    (4, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '10:00:00'), 4, 1, 2),
    (5, TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '09:00:00'), TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '10:00:00'), 5, 1, 1),
    (6, '2026-08-10 09:00:00', '2026-08-10 10:00:00', 6, 2, 2),
    (7, '2026-10-15 14:00:00', '2026-10-15 15:00:00', 7, 1, 2);

-- Ficha da Maria Cliente
INSERT IGNORE INTO anamneses (id, informacao, cliente_id, profissional_id) VALUES
    (1, 'Paciente Maria Cliente - Histórico: Paciente com pele oleosa, propensa a cravos e acne leve. Tratamento iniciado em agosto de 2026. Sensível a produtos com ácido salicílico. Preferência por procedimentos suaves.', 2, 2);
