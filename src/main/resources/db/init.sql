-- ============================================================================
-- Studio Essência - banco de dados MySQL
-- ----------------------------------------------------------------------------
-- Script unico do projeto: cria a estrutura e carrega os dados minimos de
-- demonstracao. Deve ser executado em um banco ja existente (o nome do banco e
-- definido por SPRING_DATASOURCE_URL no Backend).
--
-- No deploy, o Backend o executa quando SPRING_SQL_INIT_MODE=always:
--   SPRING_SQL_INIT_DATA_LOCATIONS=classpath:db/init.sql
--
-- Os INSERTs usam IDs fixos e INSERT IGNORE para serem idempotentes.
-- Para H2/testes o schema e gerado pelo Hibernate; este arquivo e exclusivo
-- para MySQL.
-- ============================================================================

SET NAMES utf8mb4;

-- ----------------------------------------------------------------------------
-- Tabelas de dominio
-- ----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS perfis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    perfil VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_perfis_perfil (perfil)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS status_agendamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estado VARCHAR(60) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_status_agendamentos_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS status_pagamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estado VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_status_pagamentos_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipo_pagamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tipo VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_tipo_pagamentos_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS status_clientes_pacotes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estado VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_status_clientes_pacotes_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS categoria_servicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    categoria VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categoria_servicos_categoria (categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_email (email),
    KEY idx_usuarios_perfil (perfil_id),
    CONSTRAINT fk_usuarios_perfil FOREIGN KEY (perfil_id) REFERENCES perfis (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS profissionais (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(75) NOT NULL,
    telefone VARCHAR(30),
    documento VARCHAR(75),
    foto_url VARCHAR(255),
    almoco_inicio TIME,
    almoco_fim TIME,
    usuario_id BIGINT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_profissionais_usuario (usuario_id),
    CONSTRAINT fk_profissionais_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(75) NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    documento VARCHAR(75),
    foto_url VARCHAR(255),
    bloqueado_motivo VARCHAR(255),
    lgpd_consentimento BOOLEAN DEFAULT TRUE,
    usuario_id BIGINT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_clientes_usuario (usuario_id),
    CONSTRAINT fk_clientes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS servicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    foto_url VARCHAR(255),
    duracao_minutos INT NOT NULL DEFAULT 60,
    preco DOUBLE NOT NULL DEFAULT 0.00,
    sinal_valor DOUBLE NOT NULL DEFAULT 0.00,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fk_categoria_servico BIGINT,
    PRIMARY KEY (id),
    KEY idx_servicos_categoria (fk_categoria_servico),
    CONSTRAINT fk_servicos_categoria FOREIGN KEY (fk_categoria_servico) REFERENCES categoria_servicos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pacotes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    total_sessoes INT NOT NULL DEFAULT 1,
    preco_total DOUBLE NOT NULL DEFAULT 0.00,
    validade_dias INT NOT NULL DEFAULT 30,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    servicos_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_pacotes_servico (servicos_id),
    CONSTRAINT fk_pacotes_servico FOREIGN KEY (servicos_id) REFERENCES servicos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS servicos_profissionais (
    id BIGINT NOT NULL AUTO_INCREMENT,
    servicos_id BIGINT NOT NULL,
    profissionais_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_servico_profissional (servicos_id, profissionais_id),
    CONSTRAINT fk_servicos_profissionais_servico FOREIGN KEY (servicos_id) REFERENCES servicos (id),
    CONSTRAINT fk_servicos_profissionais_profissional FOREIGN KEY (profissionais_id) REFERENCES profissionais (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dias_de_trabalho (
    id BIGINT NOT NULL AUTO_INCREMENT,
    dia_da_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    profissional_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_dia_servico (profissional_id, dia_da_semana, servico_id),
    CONSTRAINT fk_dias_trabalho_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id),
    CONSTRAINT fk_dias_trabalho_servico FOREIGN KEY (servico_id) REFERENCES servicos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS bloqueios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP NOT NULL,
    motivo VARCHAR(255),
    profissional_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_bloqueios_periodo (profissional_id, inicio, fim),
    CONSTRAINT fk_bloqueios_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS anamneses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    informacao TEXT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_anamneses_cliente (cliente_id),
    CONSTRAINT fk_anamneses_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_anamneses_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS anamnese_clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    anamneses_id BIGINT NOT NULL,
    clientes_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_anamnese_clientes_cliente (clientes_id),
    CONSTRAINT fk_anamnese_clientes_anamnese FOREIGN KEY (anamneses_id) REFERENCES anamneses (id),
    CONSTRAINT fk_anamnese_clientes_cliente FOREIGN KEY (clientes_id) REFERENCES clientes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS agendamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cancelamento_motivo VARCHAR(255),
    cancelado_em TIMESTAMP NULL,
    qtd_remarcacoes INT DEFAULT 0,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    preco DOUBLE NOT NULL DEFAULT 0.00,
    desconto_porcentagem DOUBLE DEFAULT 0.00,
    preco_final DOUBLE NOT NULL DEFAULT 0.00,
    criado_por_usuario_id BIGINT,
    cliente_id BIGINT NOT NULL,
    pacote_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    status_agendamento_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_agendamentos_cliente (cliente_id),
    KEY idx_agendamentos_profissional (profissional_id),
    KEY idx_agendamentos_status (status_agendamento_id),
    CONSTRAINT fk_agendamentos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_agendamentos_pacote FOREIGN KEY (pacote_id) REFERENCES pacotes (id),
    CONSTRAINT fk_agendamentos_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id),
    CONSTRAINT fk_agendamentos_status FOREIGN KEY (status_agendamento_id) REFERENCES status_agendamentos (id),
    CONSTRAINT fk_agendamentos_criado_por FOREIGN KEY (criado_por_usuario_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS agendamento_itens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    inicio_atendimento TIMESTAMP NOT NULL,
    fim_atendimento TIMESTAMP NOT NULL,
    checkin_em TIMESTAMP NULL,
    agendamento_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_itens_agendamento (agendamento_id),
    KEY idx_itens_profissional_inicio (profissional_id, inicio_atendimento),
    CONSTRAINT fk_itens_agendamento FOREIGN KEY (agendamento_id) REFERENCES agendamentos (id),
    CONSTRAINT fk_itens_servico FOREIGN KEY (servico_id) REFERENCES servicos (id),
    CONSTRAINT fk_itens_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pagamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    valor DOUBLE NOT NULL DEFAULT 0.00,
    pago_em TIMESTAMP NULL,
    comprovante_url VARCHAR(255),
    agendamento_id BIGINT NOT NULL,
    status_pagamento_id BIGINT NOT NULL,
    tipo_pagamentos_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_pagamentos_agendamento (agendamento_id),
    CONSTRAINT fk_pagamentos_agendamento FOREIGN KEY (agendamento_id) REFERENCES agendamentos (id),
    CONSTRAINT fk_pagamentos_status FOREIGN KEY (status_pagamento_id) REFERENCES status_pagamentos (id),
    CONSTRAINT fk_pagamentos_tipo FOREIGN KEY (tipo_pagamentos_id) REFERENCES tipo_pagamentos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comprovantes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    arquivo LONGTEXT NOT NULL,
    tipo_arquivo VARCHAR(100) NOT NULL,
    pagamento_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_comprovantes_pagamento (pagamento_id),
    CONSTRAINT fk_comprovantes_pagamento FOREIGN KEY (pagamento_id) REFERENCES pagamentos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cliente_pacotes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sessoes_restantes INT NOT NULL DEFAULT 0,
    valido_ate TIMESTAMP NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    pacote_id BIGINT NOT NULL,
    pagamento_id BIGINT,
    status_cliente_pacote_id BIGINT,
    PRIMARY KEY (id),
    KEY idx_cliente_pacotes_cliente (cliente_id),
    CONSTRAINT fk_cliente_pacotes_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_cliente_pacotes_pacote FOREIGN KEY (pacote_id) REFERENCES pacotes (id),
    CONSTRAINT fk_cliente_pacotes_pagamento FOREIGN KEY (pagamento_id) REFERENCES pagamentos (id),
    CONSTRAINT fk_cliente_pacotes_status FOREIGN KEY (status_cliente_pacote_id) REFERENCES status_clientes_pacotes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS otp_codigos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    destino VARCHAR(255) NOT NULL,
    codigo_hash VARCHAR(255) NOT NULL,
    expira_em TIMESTAMP NOT NULL,
    tentativas INT NOT NULL DEFAULT 0,
    verificado BOOLEAN NOT NULL DEFAULT FALSE,
    reset_token VARCHAR(255),
    reset_token_expira_em TIMESTAMP NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_otp_destino (destino),
    KEY idx_otp_reset_token (reset_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------------------
-- Dados de referencia
-- ----------------------------------------------------------------------------

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
    (10, 'no-show'),
    (11, 'aguardando sinal'),
    (12, 'check-in'),
    (13, 'em atendimento');

-- Compatibilidade com bancos criados antes da padronizacao do nome.
-- Garante a existencia do estado canonico mesmo se o ID 10 ja estiver ocupado
-- por 'faltou'; depois reaponta os agendamentos e remove o estado antigo.
INSERT IGNORE INTO status_agendamentos (estado) VALUES ('no-show');

UPDATE agendamentos a
JOIN status_agendamentos antigo ON antigo.estado = 'faltou'
JOIN status_agendamentos novo ON novo.estado = 'no-show'
SET a.status_agendamento_id = novo.id;

DELETE FROM status_agendamentos
WHERE estado = 'faltou';

INSERT IGNORE INTO status_pagamentos (id, estado) VALUES
    (1, 'cancelado'),
    (2, 'pendente'),
    (3, 'pago');

INSERT IGNORE INTO tipo_pagamentos (id, tipo) VALUES
    (1, 'sinal');

INSERT IGNORE INTO status_clientes_pacotes (id, estado) VALUES
    (1, 'ativo'),
    (2, 'concluido'),
    (3, 'expirado');

INSERT IGNORE INTO categoria_servicos (id, categoria) VALUES
    (1, 'Corporal'),
    (2, 'Facial');

-- ----------------------------------------------------------------------------
-- Catalogo
-- ----------------------------------------------------------------------------

INSERT IGNORE INTO servicos (
    id, nome, descricao, foto_url, duracao_minutos, preco, sinal_valor,
    ativo, criado_em, fk_categoria_servico
) VALUES
    (
        1,
        'Limpeza de pele',
        'Procedimento para remover impurezas, células mortas, cravos e miliuns da superfície do rosto.',
        'https://bucket-studio-teste-essencia-comprovante.s3.us-east-1.amazonaws.com/servicos/limpeza-de-pele.jpg',
        60, 150.00, 50.00, TRUE, CURRENT_TIMESTAMP, 1
    ),
    (
        2,
        'Remoção de cravos',
        'Procedimento localizado para remoção de cravos e comedões.',
        'https://bucket-studio-teste-essencia-comprovante.s3.us-east-1.amazonaws.com/servicos/remocao-de-cravos.jfif',
        60, 150.00, 50.00, TRUE, CURRENT_TIMESTAMP, 2
    );

INSERT IGNORE INTO pacotes (
    id, nome, total_sessoes, preco_total, validade_dias, ativo, criado_em, servicos_id
) VALUES
    (1, 'Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, CURRENT_TIMESTAMP, 1),
    (2, 'Limpeza de Pele Avulsa', 1, 150.00, 30, TRUE, CURRENT_TIMESTAMP, 1),
    (3, 'Remoção de Cravos Avulsa', 1, 150.00, 30, TRUE, CURRENT_TIMESTAMP, 2),
    (4, 'Kit Remoção de Cravos 5x', 5, 700.00, 30, TRUE, CURRENT_TIMESTAMP, 2),
    (5, 'Kit Remoção de Cravos 10x', 10, 1500.00, 30, TRUE, CURRENT_TIMESTAMP, 2);

-- ----------------------------------------------------------------------------
-- Contas de demonstracao
-- Senha de todos os usuarios de demonstracao: Studio@123
-- ----------------------------------------------------------------------------

INSERT IGNORE INTO usuarios (id, email, senha, perfil_id, criado_em, ativo) VALUES
    (1, 'admin@studioessencia.com', '$2b$10$zjlnKdbWb/uufCcW3ns4he6jwsM.hmLKEOhitYxjwgbyJWy1ISVHe', 1, CURRENT_TIMESTAMP, TRUE),
    (2, 'profissional@studioessencia.com', '$2b$10$zjlnKdbWb/uufCcW3ns4he6jwsM.hmLKEOhitYxjwgbyJWy1ISVHe', 2, CURRENT_TIMESTAMP, TRUE),
    (3, 'cliente@studioessencia.com', '$2b$10$zjlnKdbWb/uufCcW3ns4he6jwsM.hmLKEOhitYxjwgbyJWy1ISVHe', 3, CURRENT_TIMESTAMP, TRUE),
    (4, 'maria@studioessencia.com', '$2b$10$zjlnKdbWb/uufCcW3ns4he6jwsM.hmLKEOhitYxjwgbyJWy1ISVHe', 3, CURRENT_TIMESTAMP, TRUE);

INSERT IGNORE INTO profissionais (
    id, nome, telefone, documento, almoco_inicio, almoco_fim, usuario_id
) VALUES
    (1, 'Beatriz Administradora', '11988887777', '11111111111', '12:00:00', '13:00:00', 1),
    (2, 'Isabelly Profissional', '11977776666', NULL, NULL, NULL, 2);

INSERT IGNORE INTO clientes (
    id, nome, telefone, documento, bloqueado_motivo, lgpd_consentimento, usuario_id
) VALUES
    (1, 'Cliente Teste', '11999999999', '12345678900', NULL, TRUE, 3),
    (2, 'Maria Cliente', '11988885555', '98765432100', NULL, TRUE, 4);

INSERT IGNORE INTO servicos_profissionais (id, servicos_id, profissionais_id) VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 1, 2),
    (4, 2, 2);

INSERT IGNORE INTO dias_de_trabalho (
    id, dia_da_semana, hora_inicio, hora_fim, profissional_id, servico_id
) VALUES
    (1, 'MONDAY', '09:00:00', '18:00:00', 1, 1),
    (2, 'TUESDAY', '09:00:00', '18:00:00', 1, 1),
    (3, 'WEDNESDAY', '09:00:00', '18:00:00', 1, 1),
    (4, 'THURSDAY', '09:00:00', '18:00:00', 1, 1),
    (5, 'FRIDAY', '09:00:00', '18:00:00', 1, 1),
    (6, 'TUESDAY', '09:00:00', '18:00:00', 1, 2),
    (7, 'WEDNESDAY', '09:00:00', '18:00:00', 1, 2),
    (8, 'THURSDAY', '09:00:00', '18:00:00', 1, 2),
    (9, 'MONDAY', '08:00:00', '19:00:00', 2, 1),
    (10, 'MONDAY', '08:00:00', '19:00:00', 2, 2),
    (11, 'TUESDAY', '08:00:00', '19:00:00', 2, 1),
    (12, 'TUESDAY', '08:00:00', '19:00:00', 2, 2),
    (13, 'WEDNESDAY', '08:00:00', '19:00:00', 2, 1),
    (14, 'WEDNESDAY', '08:00:00', '19:00:00', 2, 2),
    (15, 'THURSDAY', '08:00:00', '19:00:00', 2, 1),
    (16, 'THURSDAY', '08:00:00', '19:00:00', 2, 2),
    (17, 'FRIDAY', '08:00:00', '19:00:00', 2, 1),
    (18, 'FRIDAY', '08:00:00', '19:00:00', 2, 2),
    (19, 'SATURDAY', '08:00:00', '14:00:00', 2, 1),
    (20, 'SATURDAY', '08:00:00', '14:00:00', 2, 2);

-- ----------------------------------------------------------------------------
-- Agenda de demonstracao (datas relativas para nao expirarem)
-- ----------------------------------------------------------------------------

INSERT IGNORE INTO bloqueios (id, inicio, fim, motivo, profissional_id) VALUES
    (
        1,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '12:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '14:00:00'),
        'Almoço/reunião',
        1
    ),
    (
        2,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '10:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '11:30:00'),
        'Compromisso pessoal',
        2
    );

INSERT IGNORE INTO agendamentos (
    id, criado_em, preco, desconto_porcentagem, preco_final,
    criado_por_usuario_id, cliente_id, pacote_id, profissional_id, status_agendamento_id
) VALUES
    (1, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -20 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 1, 9),
    (2, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -8 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 2, 2),
    (3, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -18 DAY), 150.00, 0.00, 150.00, 3, 1, 3, 2, 9),
    (4, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -3 DAY), 150.00, 0.00, 150.00, 3, 1, 2, 2, 5),
    (5, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -1 DAY), 150.00, 0.00, 150.00, 3, 2, 2, 1, 2),
    (6, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -23 DAY), 700.00, 0.00, 700.00, 3, 2, 3, 1, 9),
    (7, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -2 DAY), 150.00, 0.00, 150.00, 3, 2, 2, 2, 2),
    (8, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -7 DAY), 150.00, 0.00, 150.00, 3, 2, 2, 1, 10);

INSERT IGNORE INTO agendamento_itens (
    id, inicio_atendimento, fim_atendimento, checkin_em,
    agendamento_id, servico_id, profissional_id
) VALUES
    (
        1,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -20 DAY), '10:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -20 DAY), '11:00:00'),
        NULL, 1, 1, 1
    ),
    (
        2,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '14:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '15:00:00'),
        NULL, 2, 1, 2
    ),
    (
        3,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -13 DAY), '09:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -13 DAY), '10:00:00'),
        NULL, 3, 2, 2
    ),
    (
        4,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '09:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '10:00:00'),
        NULL, 4, 1, 2
    ),
    (
        5,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '09:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '10:00:00'),
        NULL, 5, 1, 1
    ),
    (
        6,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -23 DAY), '09:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -23 DAY), '10:00:00'),
        NULL, 6, 2, 2
    ),
    (
        7,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '14:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '15:00:00'),
        NULL, 7, 1, 2
    ),
    (
        8,
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY), '16:00:00'),
        TIMESTAMP(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY), '17:00:00'),
        NULL, 8, 1, 1
    );

-- Pagamentos de sinal ja confirmados (sustentam os indicadores financeiros).
INSERT IGNORE INTO pagamentos (
    id, valor, pago_em, comprovante_url, agendamento_id, status_pagamento_id, tipo_pagamentos_id
) VALUES
    (1, 50.00, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -20 DAY), NULL, 1, 3, 1),
    (2, 50.00, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -18 DAY), NULL, 3, 3, 1);

-- Ficha de anamnese da Maria.
INSERT IGNORE INTO anamneses (id, cliente_id, profissional_id, informacao) VALUES
    (
        1,
        2,
        2,
        'Paciente com pele oleosa, propensa a cravos e acne leve. Tratamento iniciado em agosto de 2026. Sensível a produtos com ácido salicílico. Preferência por procedimentos suaves.'
    );

INSERT IGNORE INTO anamnese_clientes (id, anamneses_id, clientes_id) VALUES
    (1, 1, 2);
