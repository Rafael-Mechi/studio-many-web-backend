package many.studio.web_backend.dto.agendamento;

import many.studio.web_backend.entity.StatusAgendamento;

import java.util.List;

public class AgendamentoResponse {

    private Long id;
    private StatusAgendamento status;
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

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
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
