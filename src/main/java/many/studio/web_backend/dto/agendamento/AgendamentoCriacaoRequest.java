package many.studio.web_backend.dto.agendamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCriacaoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long statusAgendamentoId;

    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Long criadoPorUsuarioId;

    @NotEmpty
    @Valid
    private List<AgendamentoItemCriacaoRequest> itens;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getStatusAgendamentoId() { return statusAgendamentoId; }
    public void setStatusAgendamentoId(Long statusAgendamentoId) { this.statusAgendamentoId = statusAgendamentoId; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public Long getCriadoPorUsuarioId() { return criadoPorUsuarioId; }
    public void setCriadoPorUsuarioId(Long criadoPorUsuarioId) { this.criadoPorUsuarioId = criadoPorUsuarioId; }
    public List<AgendamentoItemCriacaoRequest> getItens() { return itens; }
    public void setItens(List<AgendamentoItemCriacaoRequest> itens) { this.itens = itens; }
}