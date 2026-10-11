package many.studio.web_backend.dto.usuario;

import java.time.LocalDate;

public class MeuPerfilClienteResponse {

    private Long id;
    private Long clienteId;
    private String nome;
    private String email;
    private String telefone;
    private String clienteTelefone;
    private String cpf;
    private Boolean ativo;
    private LocalDate membroDesde;
    private String role;
    private Stats stats;

    public static class Stats {
        private Integer totalAgendamentos;
        private Integer totalFichas;

        public Stats() {
        }

        public Stats(Integer totalAgendamentos, Integer totalFichas) {
            this.totalAgendamentos = totalAgendamentos;
            this.totalFichas = totalFichas;
        }

        public Integer getTotalAgendamentos() {
            return totalAgendamentos;
        }

        public void setTotalAgendamentos(Integer totalAgendamentos) {
            this.totalAgendamentos = totalAgendamentos;
        }

        public Integer getTotalFichas() {
            return totalFichas;
        }

        public void setTotalFichas(Integer totalFichas) {
            this.totalFichas = totalFichas;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getMembroDesde() {
        return membroDesde;
    }

    public void setMembroDesde(LocalDate membroDesde) {
        this.membroDesde = membroDesde;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
