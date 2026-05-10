CREATE TABLE IF NOT EXISTS status_pagamentos (
                                                 id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                 estado VARCHAR(45)
    );

CREATE TABLE IF NOT EXISTS status_clientes_pacotes (
                                                       id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                       estado VARCHAR(45)
    );

CREATE TABLE IF NOT EXISTS anamneses (
                                         id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                         informacao VARCHAR(45),
    arquivo_url VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS status_agendamentos (
                                                   id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                   estado VARCHAR(45)
    );

CREATE TABLE IF NOT EXISTS perfis (
                                      id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                      perfil VARCHAR(45) NOT NULL
    );

CREATE TABLE IF NOT EXISTS usuarios (
                                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP,

    perfil_id INT NOT NULL,

    FOREIGN KEY (perfil_id) REFERENCES perfis(id)
    );

CREATE TABLE IF NOT EXISTS profissionais (
                                             id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                             nome VARCHAR(75),

    usuario_id INT UNIQUE,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );

CREATE TABLE IF NOT EXISTS bloqueios (
                                         id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                         inicio TIMESTAMP NOT NULL,
                                         fim TIMESTAMP NOT NULL,
                                         motivo VARCHAR(255),

    profissional_id INT,

    FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
    );

CREATE TABLE IF NOT EXISTS servicos (
                                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        nome VARCHAR(45) NOT NULL,
    descricao VARCHAR(255),
    foto_url VARCHAR(255),
    duracao_minutos INT,
    preco DECIMAL(8,2),
    sinal_valor DECIMAL(8,2),
    ativo BOOLEAN,
    criado_em TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS clientes (
                                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        nome VARCHAR(75) NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    documento VARCHAR(75),
    total_no_shows INT,
    bloqueado_motivo VARCHAR(255),
    lgpd_consentimento BOOLEAN,

    usuario_id INT UNIQUE,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );

CREATE TABLE IF NOT EXISTS anamnese_clientes (
                                                 id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                 anamneses_id INT NOT NULL,
                                                 clientes_id INT NOT NULL,

                                                 FOREIGN KEY (anamneses_id) REFERENCES anamneses(id),
    FOREIGN KEY (clientes_id) REFERENCES clientes(id)
    );

CREATE TABLE IF NOT EXISTS servicos_profissionais (
                                                      id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                      servicos_id INT NOT NULL,
                                                      profissionais_id INT NOT NULL,

                                                      FOREIGN KEY (servicos_id) REFERENCES servicos(id),
    FOREIGN KEY (profissionais_id) REFERENCES profissionais(id)
    );

CREATE TABLE IF NOT EXISTS pacotes (
                                       id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                       nome VARCHAR(45) NOT NULL,
    total_sessoes INT,
    preco_total DECIMAL(8,2),
    validade_dias INT,
    ativo BOOLEAN,
    criado_em TIMESTAMP,

    servicos_id INT,

    FOREIGN KEY (servicos_id) REFERENCES servicos(id)
    );

CREATE TABLE IF NOT EXISTS agendamentos (
                                            id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                            inicio TIMESTAMP,
                                            fim TIMESTAMP,
                                            cancelamento_motivo VARCHAR(255),
    cancelado_em TIMESTAMP,
    qtd_remarcacoes INT,
    remarcacao_aprovacao_necessaria BOOLEAN,
    criado_por_usuario_id INT,
    criado_em TIMESTAMP,

    cliente_id INT,
    status_agendamento_id INT,

    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (status_agendamento_id) REFERENCES status_agendamentos(id),
    FOREIGN KEY (criado_por_usuario_id) REFERENCES usuarios(id)
    );

CREATE TABLE IF NOT EXISTS pagamentos (
                                          id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                          valor DECIMAL(8,2),
    pago_em TIMESTAMP,
    comprovante_url VARCHAR(255),

    agendamento_id INT,
    status_pagamento_id INT,

    FOREIGN KEY (agendamento_id) REFERENCES agendamentos(id),
    FOREIGN KEY (status_pagamento_id) REFERENCES status_pagamentos(id)
    );

CREATE TABLE IF NOT EXISTS cliente_pacotes (
                                               id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                               sessoes_restantes INT,
                                               valido_ate TIMESTAMP,
                                               criado_em TIMESTAMP,

                                               cliente_id INT,
                                               pacote_id INT,
                                               pagamento_id INT,
                                               status_cliente_pacote_id INT,

                                               FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (pacote_id) REFERENCES pacotes(id),
    FOREIGN KEY (pagamento_id) REFERENCES pagamentos(id),
    FOREIGN KEY (status_cliente_pacote_id) REFERENCES status_clientes_pacotes(id)
    );

CREATE TABLE IF NOT EXISTS agendamento_itens (
                                                 id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                                 inicio_atendimento TIMESTAMP,
                                                 fim_atendimento TIMESTAMP,
                                                 checkin_em TIMESTAMP,
                                                 preco DECIMAL(8,2),
    desconto_porcentagem DECIMAL(5,2),
    preco_final DECIMAL(8,2),

    agendamento_id INT,
    servico_id INT,
    profissional_id INT,

    FOREIGN KEY (agendamento_id) REFERENCES agendamentos(id),
    FOREIGN KEY (servico_id) REFERENCES servicos(id),
    FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
    );