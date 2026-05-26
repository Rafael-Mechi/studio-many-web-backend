package many.studio.web_backend.dto.agendamento;

import many.studio.web_backend.entity.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCriacaoResponse {

    private Long id;
    private LocalDateTime criadoEm;
    private Double preco;
    private Double descontoPorcentagem;
    private Double precoFinal;
    private String status;
    private List<AgendamentoItemResponse> itens;
    private ClienteDto cliente;
    private ProfissionalDto profissional;
    private UsuarioDto criadoPor;
    private PacoteDto pacote;

    public static class ClienteDto {
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

    public static class ProfissionalDto {
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

    public static class UsuarioDto {
        private Long id;
        private String email;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class PacoteDto {
        private Long id;
        private String nome;
        private Integer totalSessoes;
        private Integer validadeDias;
        private ServicoDto servico;

        public static class ServicoDto {
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

        public Integer getValidadeDias() {
            return validadeDias;
        }

        public void setValidadeDias(Integer validadeDias) {
            this.validadeDias = validadeDias;
        }

        public ServicoDto getServico() {
            return servico;
        }

        public void setServico(ServicoDto servico) {
            this.servico = servico;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getDescontoPorcentagem() {
        return descontoPorcentagem;
    }

    public void setDescontoPorcentagem(Double descontoPorcentagem) {
        this.descontoPorcentagem = descontoPorcentagem;
    }

    public Double getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(Double precoFinal) {
        this.precoFinal = precoFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AgendamentoItemResponse> getItens() {
        return itens;
    }

    public void setItens(List<AgendamentoItemResponse> itens) {
        this.itens = itens;
    }

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public ProfissionalDto getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalDto profissional) {
        this.profissional = profissional;
    }

    public UsuarioDto getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(UsuarioDto criadoPor) {
        this.criadoPor = criadoPor;
    }

    public PacoteDto getPacote() {
        return pacote;
    }

    public void setPacote(PacoteDto pacote) {
        this.pacote = pacote;
    }
}