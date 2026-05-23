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

INSERT INTO usuarios (email, senha, perfil_id, criado_em) VALUES ('ana.estetica@email.com', 'hash123', 2, NOW());
INSERT INTO profissionais (nome, usuario_id) VALUES ('Ana Silva', 1);

INSERT INTO servicos (nome, preco, duracao_minutos, ativo) VALUES ('Limpeza de Pele', 150.00, 60, TRUE);
INSERT INTO pacotes (nome, total_sessoes, preco_total, validade_dias, ativo, servicos_id) VALUES ('Combo Verão 5x Limpeza', 5, 600.00, 90, TRUE, 1);

INSERT INTO usuarios (email, senha, perfil_id) VALUES ('cliente.joana@email.com', 'hash456', 3);
INSERT INTO clientes (nome, telefone, usuario_id) VALUES ('Joana Santos', '11999999999', 2);
INSERT INTO anamneses (informacao) VALUES ('Ficha Inicial Joana');
INSERT INTO anamnese_clientes (anamneses_id, clientes_id) VALUES (1, 1);

INSERT INTO agendamentos (id, inicio, fim, cancelamento_motivo, cancelado_em, qtd_remarcacoes, remarcacao_aprovacao_necessaria, criado_por_usuario_id, criado_em, cliente_id, status_agendamento_id)
VALUES
(1, '2026-12-30 12:00:00', '2026-12-30 12:15:00', null, null, 0, true, 1, '2026-12-28 11:00:00', 1, 2);

INSERT INTO agendamento_itens(id, inicio_atendimento, fim_atendimento, checkin_em, preco, desconto_porcentagem, preco_final, agendamento_id, servico_id, profissional_id)
VALUES
(1, null, null, null, 150.00, 0, 150.00, 1, 1, 1);