package many.studio.web_backend.dto.agendamento;

import many.studio.web_backend.entity.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCriacaoResponse {

    private Long id;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private LocalDateTime criadoEm;
    private StatusAgendamento status;
    private Long criadoPorUsuarioId;
    private ClienteResponse cliente;
    private List<AgendamentoItemResponse> itens;

    public static class ClienteResponse {
        private Long id;
        private String nome;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public Long getCriadoPorUsuarioId() { return criadoPorUsuarioId; }
    public void setCriadoPorUsuarioId(Long criadoPorUsuarioId) { this.criadoPorUsuarioId = criadoPorUsuarioId; }
    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }
    public List<AgendamentoItemResponse> getItens() { return itens; }
    public void setItens(List<AgendamentoItemResponse> itens) { this.itens = itens; }
}