package many.studio.web_backend.dto.agendamento;

import many.studio.web_backend.entity.StatusAgendamento;
import many.studio.web_backend.entity.enums.StatusAgendamentoItem;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCriacaoResponse {

    private Long id;
    private LocalDateTime criadoEm;
    private StatusAgendamento status;
    private Long criadoPorId;
    private ClienteResponse cliente;
    private ProfissionalResponse profissional;
    private PacoteResponse pacote;
    private List<AgendamentoItemResponse> itens;

    public static class ClienteResponse {

        private Long id;
        private String nome;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public static class ProfissionalResponse {

        private Long id;
        private String nome;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public static class PacoteResponse {

        private Long id;
        private String nome;
        private Integer totalSessoes;
        private Double precoTotal;
        private Integer validadeDias;
        private Boolean ativo;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getTotalSessoes() {
            return totalSessoes;
        }

        public void setTotalSessoes(Integer totalSessoes) {
            this.totalSessoes = totalSessoes;
        }

        public Double getPrecoTotal() {
            return precoTotal;
        }

        public void setPrecoTotal(Double precoTotal) {
            this.precoTotal = precoTotal;
        }

        public Integer getValidadeDias() {
            return validadeDias;
        }

        public void setValidadeDias(Integer validadeDias) {
            this.validadeDias = validadeDias;
        }

        public Boolean getAtivo() {
            return ativo;
        }

        public void setAtivo(Boolean ativo) {
            this.ativo = ativo;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Long getCriadoPorId() {
        return criadoPorId;
    }

    public void setCriadoPorId(Long criadoPorId) {
        this.criadoPorId = criadoPorId;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }

    public ProfissionalResponse getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalResponse profissional) {
        this.profissional = profissional;
    }

    public PacoteResponse getPacote() {
        return pacote;
    }

    public void setPacote(PacoteResponse pacote) {
        this.pacote = pacote;
    }

    public List<AgendamentoItemResponse> getItens() {
        return itens;
    }

    public void setItens(List<AgendamentoItemResponse> itens) {
        this.itens = itens;
    }
}
