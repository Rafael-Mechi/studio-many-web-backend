package many.studio.web_backend.dto.agendamento;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class AgendamentoAnaliseResponse {
    private Long idAgendamento;
    private Long idCliente;
    private String nomeCliente;
    private LocalDateTime dataHoraAgendamento;

    private Integer qtdNoShows;
    private Long qtdAgendamentosNaoFinalizados;
    private Long qtdAgendamentosConcluidos;

    private long tempoDiasAteConsulta;

    // Sobrecarga para IDEs/provedores que inferem os COUNTs como Integer.
    // Mantém também o construtor com Long, usado normalmente por COUNT em JPQL.
    public AgendamentoAnaliseResponse(
            Long idAgendamento,
            Long idCliente,
            String nomeCliente,
            LocalDateTime dataHoraAgendamento,
            int qtdNoShows,
            int qtdAgendamentosNaoFinalizados,
            int qtdAgendamentosConcluidos) {
        this(
                idAgendamento,
                idCliente,
                nomeCliente,
                dataHoraAgendamento,
                Long.valueOf(qtdNoShows),
                Long.valueOf(qtdAgendamentosNaoFinalizados),
                Long.valueOf(qtdAgendamentosConcluidos)
        );
    }

    public AgendamentoAnaliseResponse(
            Long idAgendamento,
            Long idCliente,
            String nomeCliente,
            LocalDateTime dataHoraAgendamento,
            Long qtdNoShows,
            Long qtdAgendamentosNaoFinalizados,
            Long qtdAgendamentosConcluidos) {

        this.idAgendamento = idAgendamento;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.dataHoraAgendamento = dataHoraAgendamento;
        this.qtdNoShows = qtdNoShows != null ? qtdNoShows.intValue() : 0;
        this.qtdAgendamentosNaoFinalizados =
                qtdAgendamentosNaoFinalizados;
        this.qtdAgendamentosConcluidos = qtdAgendamentosConcluidos;

        calcularTempoDiasAteConsulta();
    }

    private void calcularTempoDiasAteConsulta() {
        LocalDate hoje = LocalDate.now(
                ZoneId.of("America/Sao_Paulo")
        );

        this.tempoDiasAteConsulta = Math.max(
                0,
                ChronoUnit.DAYS.between(
                        hoje,
                        dataHoraAgendamento.toLocalDate()
                )
        );
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public Integer getQtdNoShows() {
        return qtdNoShows;
    }

    public Long getQtdAgendamentosNaoFinalizados() {
        return qtdAgendamentosNaoFinalizados;
    }

    public Long getQtdAgendamentosConcluidos() {
        return qtdAgendamentosConcluidos;
    }

    public long getTempoDiasAteConsulta() {
        return tempoDiasAteConsulta;
    }
}
