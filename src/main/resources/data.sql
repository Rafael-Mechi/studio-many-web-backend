INSERT INTO perfis (perfil) VALUES
                                ('ROLE_ADMIN'),
                                ('ROLE_PROFISSIONAL'),
                                ('ROLE_CLIENTE');

INSERT INTO status_agendamentos (estado) VALUES
                                             ('solicitar confirmacao agendamento'), -- Pagou o sinal, falta a Bea autorizar
                                             ('agendado'), -- Cliente está agendado
                                             ('confirmado'), -- Cliente confirmou a presença na janela de 24 horas
                                             ('solicitar cancelamento'), -- Cliente ainda não confirmou na janela de 24 horas
                                             ('cancelado'), -- Serviço cancelado antes do horário do atendimento
                                             ('solicitar reagendamento'), -- Cliente pede para ser reagendado
                                             ('reagendado'), -- Serviço reagendado
                                             ('recusado'), -- Bea não autorizou
                                             ('concluido'), -- Serviço concluído
                                             ('faltou'), -- Cliente não compareceu
                                             ('em atendimento'); -- cliente chegou e está em atendimento
-- Não coloquei nada referente ao check-in, acredito que vai ficar muitos status ao mesmo tempo.
-- Tirei a parte do status = Pago. Pois acho que isso entra em outra tabela e o agendamento pode ter sido pago, mas não concluído ou outras situações semelhantes.


INSERT INTO status_pagamentos (estado) VALUES
                                           ('Cancelado'),
                                           ('Pendente'),
                                           ('Pago');

INSERT INTO tipo_pagamentos (tipo) VALUES
                                       ('PIX'),
                                       ('Dinheiro'),
                                       ('Cartão de débito'),
                                       ('Cartão de crédito');

INSERT INTO tipos_sinais (tipo) VALUES
                                    ('Sem sinal'),
                                    ('Valor Fixo'),
                                    ('Percentual');

-- 2. Criando um Usuário e um Profissional
INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('ana.estetica@email.com', 'hash123', 2, NOW());
INSERT INTO profissionais (nome, usuario_id) VALUES ('Ana Silva', 1);

-- 3. Criando um Serviço e um Pacote
INSERT INTO servicos (nome, preco, duracao_minutos, ativo, tipos_sinais_id) VALUES ('Limpeza de Pele', 150.00, 60, TRUE, 1);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, 1);

-- 4. Cadastrando um Cliente e sua Anamnese (Link para o PDF)
INSERT INTO usuarios (email, senha, perfil_id) VALUES ('cliente.joana@email.com', 'hash456', 3);
INSERT INTO clientes (nome, telefone, usuario_id) VALUES ('Joana Santos', '11999999999', 2);
INSERT INTO anamneses (informacao) VALUES ('Ficha Inicial Joana');
INSERT INTO anamnese_clientes (anamneses_id, clientes_id) VALUES (1, 1);

-- 5. Fluxo de Agendamento (O "Carrinho")
-- Primeiro criamos o cabeçalho do agendamento
-- INSERT INTO agendamentos (inicio, fim, cliente_id, status_agendamento_id, criado_em)
-- VALUES ('2023-10-25 14:00:00', '2023-10-25 15:00:00', 1, 1, NOW());
--
-- -- Depois inserimos o item (serviço) dentro desse agendamento
-- INSERT INTO agendamento_itens (agendamento_id, servico_id, profissional_id, preco, preco_final)
-- VALUES (1, 1, 1, 150.00, 150.00);
-- 
-- -- 6. Registro de Pagamento
-- INSERT INTO pagamentos (valor, pago_em, agendamento_id, status_pagamento_id)
-- VALUES (150.00, NOW(), 1, 2);