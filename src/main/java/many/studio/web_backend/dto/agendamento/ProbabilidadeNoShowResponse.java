package many.studio.web_backend.dto.agendamento;

import java.math.BigDecimal;

public class ProbabilidadeNoShowResponse {

    private final Long idAgendamento;
    private final BigDecimal probabilidadeFalta;

    public ProbabilidadeNoShowResponse(Long idAgendamento, BigDecimal probabilidadeFalta) {
        this.idAgendamento = idAgendamento;
        this.probabilidadeFalta = probabilidadeFalta;
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public BigDecimal getProbabilidadeFalta() {
        return probabilidadeFalta;
    }
}
