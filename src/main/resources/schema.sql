CREATE TABLE tipo_pagamentos(
                                id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                tipo VARCHAR(45) NOT NULL
);

CREATE TABLE status_pagamentos(
                                  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                  estado VARCHAR(45)
);

CREATE TABLE status_clientes_pacotes(
                                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        estado VARCHAR(45)
);

CREATE TABLE anamneses(
                          id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          informacao VARCHAR(45)
);

CREATE TABLE tipos_sinais(
                             id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                             tipo VARCHAR(45)
);

CREATE TABLE status_agendamentos(
                                    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                    estado VARCHAR(45)
);

CREATE TABLE perfis(
                       id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       perfil VARCHAR(45) NOT NULL
);

CREATE TABLE usuarios(
                         id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         ativo BOOLEAN DEFAULT TRUE,
                         criado_em timestamp,

                         perfil_id INT NOT NULL,

                         FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);

CREATE TABLE profissionais(
                              id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                              nome VARCHAR(75),
                              foto_url VARCHAR(75),
                              ativo BOOLEAN,
                              criado_em timestamp,

                              usuario_id INT UNIQUE,

                              FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE bloqueios(
                          id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          inicio timestamp NOT NULL,
                          fim timestamp NOT NULL,
                          motivo VARCHAR(255),

                          profissional_id INT,

                          FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
);

CREATE TABLE servicos(
                         id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         nome VARCHAR(45) NOT NULL,
                         descricao VARCHAR(255),
                         foto_url VARCHAR(255),
                         duracao_minutos INT,
                         preco DECIMAL(6,2),
                         sinal_valor DECIMAL(5,2),
                         ativo BOOLEAN,
                         criado_em timestamp,

                         tipos_sinais_id INT,

                         FOREIGN KEY (tipos_sinais_id) REFERENCES tipos_sinais(id)
);

CREATE TABLE clientes(
                         id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         nome VARCHAR(75) NOT NULL,
                         telefone VARCHAR(30) NOT NULL,
                         documento VARCHAR(75),
                         total_no_shows INT,
                         bloqueado_motivo VARCHAR(255),
                         lgpd_consentimento BOOLEAN,
                         criado_em timestamp,
                         ativo BOOLEAN,

                         usuario_id INT UNIQUE,

                         FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE anamnese_clientes(
                                  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                  anamneses_id INT NOT NULL,
                                  clientes_id INT NOT NULL,

                                  FOREIGN KEY (anamneses_id) REFERENCES anamneses(id),
                                  FOREIGN KEY (clientes_id) REFERENCES clientes(id)
);

CREATE TABLE servicos_profissionais(
                                       id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                       servicos_id INT NOT NULL,
                                       profissionais_id INT NOT NULL,

                                       FOREIGN KEY (servicos_id) REFERENCES servicos(id),
                                       FOREIGN KEY (profissionais_id) REFERENCES profissionais(id)
);

CREATE TABLE pacotes(
                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                        nome VARCHAR(45) NOT NULL,
                        total_sessoes INT,
                        preco_total DECIMAL(6,2),
                        validade_dias INT,
                        ativo BOOLEAN,

                        servicos_id INT,

                        FOREIGN KEY (servicos_id) REFERENCES servicos(id)
);

CREATE TABLE agendamentos(
                             id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                             inicio timestamp,
                             fim timestamp,
                             cancelamento_motivo VARCHAR(255),
                             cancelado_em timestamp,
                             qtd_remarcacoes INT,
                             remarcacao_aprovacao_necessaria BOOLEAN,
                             checkin_em timestamp,
                             inicio_atendimento timestamp,
                             fim_atendimento timestamp,
                             criado_por_usuario_id INT,
                             criado_em timestamp,

                             cliente_id INT,
                             status_agendamento_id INT,

                             FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                             FOREIGN KEY (status_agendamento_id) REFERENCES status_agendamentos(id),
                             FOREIGN KEY (criado_por_usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE pagamentos(
                           id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           valor DECIMAL(6,2),
                           pago_em timestamp,
                           comprovante_url VARCHAR(255),

                           agendamento_id INT,
                           status_pagamento_id INT,
                           tipo_pagamentos_id INT,

                           FOREIGN KEY (agendamento_id) REFERENCES agendamentos(id),
                           FOREIGN KEY (status_pagamento_id) REFERENCES status_pagamentos(id),
                           FOREIGN KEY (tipo_pagamentos_id) REFERENCES tipo_pagamentos(id)
);

CREATE TABLE cliente_pacotes(
                                id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                sessoes_restantes INT,
                                valido_ate timestamp,
                                criado_em timestamp,

                                cliente_id INT,
                                pacote_id INT,
                                pagamento_id INT,
                                status_cliente_pacote_id INT,

                                FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                                FOREIGN KEY (pacote_id) REFERENCES pacotes(id),
                                FOREIGN KEY (pagamento_id) REFERENCES pagamentos(id),
                                FOREIGN KEY (status_cliente_pacote_id) REFERENCES status_clientes_pacotes(id)
);

CREATE TABLE agendamento_itens(
                                  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                  inicio timestamp,
                                  fim timestamp,
                                  preco DECIMAL(6,2),
                                  desconto_porcentagem DECIMAL(5,2),
                                  preco_final DECIMAL(6,2),

                                  agendamento_id INT,
                                  servico_id INT,
                                  profissional_id INT,

                                  FOREIGN KEY (agendamento_id) REFERENCES agendamentos(id),
                                  FOREIGN KEY (servico_id) REFERENCES servicos(id),
                                  FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
);