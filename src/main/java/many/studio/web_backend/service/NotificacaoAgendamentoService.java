package many.studio.web_backend.service;

import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.StatusAgendamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Monta e publica as mensagens de WhatsApp relacionadas ao ciclo de vida de um
 * agendamento.
 *
 * <p>O texto é montado ainda dentro da transação (onde as relações JPA estão
 * disponíveis) e o envio é agendado para depois do commit. Assim, uma alteração
 * de status que sofra rollback não gera mensagem para o cliente.</p>
 */
@Service
public class NotificacaoAgendamentoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoAgendamentoService.class);
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", PT_BR);

    private final NotificacaoPublisher notificacaoPublisher;

    public NotificacaoAgendamentoService(NotificacaoPublisher notificacaoPublisher) {
        this.notificacaoPublisher = notificacaoPublisher;
    }

    public void notificarNovaSolicitacao(Agendamento agendamento) {
        try {
            String detalhes = detalhesAgendamento(agendamento, proximaData(agendamento));

            String mensagemCliente = """
                    Olá, %s! Sua solicitação de agendamento foi registrada.

                    %s

                    O horário será confirmado pela equipe após a validação do sinal.
                    """.formatted(nomeCliente(agendamento), detalhes);

            enviarParaCliente(agendamento, mensagemCliente);

            if (agendamento.getProfissional() != null
                    && agendamento.getProfissional().getTelefone() != null
                    && !agendamento.getProfissional().getTelefone().isBlank()) {

                String mensagemProfissional = """
                        Nova solicitação de agendamento

                        Cliente: %s
                        %s

                        Confirme ou recuse pelo painel do Studio Essência.
                        """.formatted(nomeCliente(agendamento), detalhes);

                enviar(agendamento.getProfissional().getTelefone(), mensagemProfissional);
            }
        } catch (RuntimeException ex) {
            log.error("Não foi possível montar a notificação de nova solicitação do agendamento {}",
                    agendamento.getId(), ex);
        }
    }

    public void notificarStatus(Agendamento agendamento) {
        try {
            String estado = estadoNormalizado(agendamento);
            if (estado == null) {
                return;
            }

            String mensagem = switch (estado) {
                case StatusAgendamento.AGUARDANDO_SINAL -> """
                        Olá, %s! Recebemos sua solicitação de agendamento.

                        %s

                        Estamos aguardando o pagamento do sinal para enviar o agendamento para confirmação.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.SOLICITAR_CONFIRMACAO -> """
                        Olá, %s! O sinal do seu agendamento foi recebido.

                        %s

                        Agora o horário está em análise pela equipe. Assim que for confirmado, avisaremos por aqui.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.AGENDADO -> """
                        Olá, %s! Seu agendamento está confirmado.

                        %s

                        Se precisar remarcar, entre em contato com antecedência.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.REAGENDADO -> """
                        Olá, %s! Seu agendamento foi reagendado.

                        %s

                        Confira a nova data e horário acima.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.CANCELADO -> mensagemCancelamento(agendamento);

                case StatusAgendamento.RECUSADO -> """
                        Olá, %s. Não foi possível confirmar o agendamento solicitado.

                        %s

                        Entre em contato com o studio para verificar outros horários disponíveis.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.NO_SHOW -> """
                        Olá, %s. Sentimos sua falta no atendimento.

                        %s

                        Se ainda tiver interesse, entre em contato com o studio para remarcar.
                        """.formatted(nomeCliente(agendamento), detalhesAgendamento(agendamento, proximaData(agendamento)));

                case StatusAgendamento.CONCLUIDO -> montarResumoConclusao(agendamento);

                // Etapas operacionais: não geram mensagem ao cliente.
                case StatusAgendamento.CHECK_IN, StatusAgendamento.EM_ATENDIMENTO -> null;

                default -> null;
            };

            if (mensagem != null) {
                enviarParaCliente(agendamento, mensagem);
            }
        } catch (RuntimeException ex) {
            log.error("Não foi possível montar a notificação de status do agendamento {}",
                    agendamento.getId(), ex);
        }
    }

    public void notificarReagendamento(Agendamento agendamento, AgendamentoItem item) {
        try {
            String mensagem = """
                    Olá, %s! Um horário do seu agendamento foi alterado.

                    Pacote: %s
                    Serviço: %s
                    Profissional: %s
                    Nova data/hora: %s

                    Confira os detalhes no painel do Studio Essência.
                    """.formatted(
                    nomeCliente(agendamento),
                    nomePacote(agendamento),
                    nomeServico(item, agendamento),
                    nomeProfissional(item, agendamento),
                    formatarDataHora(item.getInicioAtendimento()));

            enviarParaCliente(agendamento, mensagem);
        } catch (RuntimeException ex) {
            log.error("Não foi possível montar a notificação de reagendamento do agendamento {}",
                    agendamento.getId(), ex);
        }
    }

    private String mensagemCancelamento(Agendamento agendamento) {
        String motivo = agendamento.getCancelamentoMotivo();
        String complemento = (motivo == null || motivo.isBlank())
                ? ""
                : "\nMotivo informado: " + motivo + "\n";

        return """
                Olá, %s. Seu agendamento foi cancelado.

                %s
                %s
                Se precisar de ajuda para escolher um novo horário, é só chamar o studio.
                """.formatted(
                nomeCliente(agendamento),
                detalhesAgendamento(agendamento, proximaData(agendamento)),
                complemento);
    }

    private String montarResumoConclusao(Agendamento agendamento) {
        List<AgendamentoItem> itens = itensOrdenados(agendamento);

        StringBuilder sessoes = new StringBuilder();
        for (AgendamentoItem item : itens) {
            sessoes.append("- ")
                    .append(formatarDataHora(item.getInicioAtendimento()))
                    .append(" | ")
                    .append(nomeServico(item, agendamento))
                    .append(" | ")
                    .append(nomeProfissional(item, agendamento))
                    .append("\n");
        }

        if (sessoes.isEmpty()) {
            sessoes.append("- ").append(formatarDataHora(proximaData(agendamento))).append("\n");
        }

        int quantidadeSessoes = itens.isEmpty() ? totalSessoes(agendamento) : itens.size();

        return """
                Olá, %s! Seu atendimento foi concluído. Obrigado pela visita!

                RESUMO DO AGENDAMENTO
                Pacote: %s
                Serviço: %s
                Profissional: %s
                Sessões (%d):
                %s
                Valor total: %s

                Esperamos você na próxima!
                """.formatted(
                nomeCliente(agendamento),
                nomePacote(agendamento),
                nomeServico(agendamento),
                nomeProfissional(agendamento),
                quantidadeSessoes,
                sessoes.toString().stripTrailing(),
                formatarValor(agendamento.getPrecoFinal()));
    }

    private String detalhesAgendamento(Agendamento agendamento, LocalDateTime dataHora) {
        return """
                Pacote: %s
                Serviço: %s
                Profissional: %s
                Próxima sessão: %s
                Sessões: %d
                Valor: %s
                """.formatted(
                nomePacote(agendamento),
                nomeServico(agendamento),
                nomeProfissional(agendamento),
                formatarDataHora(dataHora),
                totalSessoes(agendamento),
                formatarValor(agendamento.getPrecoFinal()));
    }

    private void enviarParaCliente(Agendamento agendamento, String mensagem) {
        String telefone = agendamento.getCliente() == null
                ? null
                : agendamento.getCliente().getTelefone();

        enviar(telefone, mensagem);
    }

    private void enviar(String telefone, String mensagem) {
        if (telefone == null || telefone.isBlank()) {
            log.warn("Notificação não enviada: cliente/profissional sem telefone cadastrado");
            return;
        }

        Runnable envio = () -> {
            try {
                notificacaoPublisher.enviarWhatsApp(telefone, mensagem);
            } catch (RuntimeException ex) {
                // A troca de status já foi confirmada; uma falha no broker não deve
                // desfazer a operação. O worker mantém retentativas e DLQ do seu lado.
                log.error("Falha ao publicar notificação de WhatsApp para {}", telefone, ex);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    envio.run();
                }
            });
            return;
        }

        envio.run();
    }

    private List<AgendamentoItem> itensOrdenados(Agendamento agendamento) {
        if (agendamento.getItens() == null) {
            return List.of();
        }
        return agendamento.getItens().stream()
                .filter(item -> item.getInicioAtendimento() != null)
                .sorted(Comparator.comparing(AgendamentoItem::getInicioAtendimento))
                .toList();
    }

    private LocalDateTime proximaData(Agendamento agendamento) {
        List<AgendamentoItem> itens = itensOrdenados(agendamento);
        if (itens.isEmpty()) {
            return null;
        }

        LocalDateTime agora = LocalDateTime.now();
        return itens.stream()
                .map(AgendamentoItem::getInicioAtendimento)
                .filter(data -> !data.isBefore(agora))
                .findFirst()
                .orElseGet(() -> itens.getLast().getInicioAtendimento());
    }

    private String nomeCliente(Agendamento agendamento) {
        return agendamento.getCliente() != null && agendamento.getCliente().getNome() != null
                ? agendamento.getCliente().getNome()
                : "cliente";
    }

    private String nomePacote(Agendamento agendamento) {
        return agendamento.getPacote() != null && agendamento.getPacote().getNome() != null
                ? agendamento.getPacote().getNome()
                : "Não informado";
    }

    private String nomeServico(Agendamento agendamento) {
        return agendamento.getPacote() != null
                && agendamento.getPacote().getServico() != null
                && agendamento.getPacote().getServico().getNome() != null
                ? agendamento.getPacote().getServico().getNome()
                : "Não informado";
    }

    private String nomeServico(AgendamentoItem item, Agendamento agendamento) {
        return item.getServico() != null && item.getServico().getNome() != null
                ? item.getServico().getNome()
                : nomeServico(agendamento);
    }

    private String nomeProfissional(Agendamento agendamento) {
        return agendamento.getProfissional() != null
                && agendamento.getProfissional().getNome() != null
                ? agendamento.getProfissional().getNome()
                : "Não informado";
    }

    private String nomeProfissional(AgendamentoItem item, Agendamento agendamento) {
        return item.getProfissional() != null && item.getProfissional().getNome() != null
                ? item.getProfissional().getNome()
                : nomeProfissional(agendamento);
    }

    private int totalSessoes(Agendamento agendamento) {
        if (agendamento.getPacote() != null && agendamento.getPacote().getTotalSessoes() != null) {
            return agendamento.getPacote().getTotalSessoes();
        }
        return agendamento.getItens() == null ? 0 : agendamento.getItens().size();
    }

    private String estadoNormalizado(Agendamento agendamento) {
        if (agendamento.getStatusAgendamento() == null
                || agendamento.getStatusAgendamento().getEstado() == null) {
            return null;
        }
        return agendamento.getStatusAgendamento().getEstado().trim().toLowerCase(PT_BR);
    }

    private String formatarDataHora(LocalDateTime dataHora) {
        return dataHora == null ? "a definir" : DATA_HORA.format(dataHora);
    }

    private String formatarValor(Double valor) {
        return valor == null ? "a definir" : NumberFormat.getCurrencyInstance(PT_BR).format(valor);
    }
}
