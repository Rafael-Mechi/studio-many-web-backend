package many.studio.web_backend.dto.usuario;

import java.time.LocalDate;
import java.util.List;

public class ClienteResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate ultimaVisita;
    private String cpf;
    private Integer qtdNoShows;
    private Double totalGasto;
    private LocalDate clienteDesde;
    List<HistoricoAgendamentos> historicoAgendamentos;

    public ClienteResponse() {}

    public ClienteResponse(Long id, String nome, String telefone, String email, LocalDate ultimaVisita, String cpf, Integer qtdNoShows, Double totalGasto, LocalDate clienteDesde, List<HistoricoAgendamentos> historicoAgendamentos) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.ultimaVisita = ultimaVisita;
        this.cpf = cpf;
        this.qtdNoShows = qtdNoShows;
        this.totalGasto = totalGasto;
        this.clienteDesde = clienteDesde;
        this.historicoAgendamentos = historicoAgendamentos;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getQtdNoShows() {
        return qtdNoShows;
    }

    public void setQtdNoShows(Integer qtdNoShows) {
        this.qtdNoShows = qtdNoShows;
    }

    public Double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(Double totalGasto) {
        this.totalGasto = totalGasto;
    }

    public LocalDate getClienteDesde() {
        return clienteDesde;
    }

    public void setClienteDesde(LocalDate clienteDesde) {
        this.clienteDesde = clienteDesde;
    }

    public List<HistoricoAgendamentos> getHistoricoAgendamentos() {
        return historicoAgendamentos;
    }

    public void setHistoricoAgendamentos(List<HistoricoAgendamentos> historicoAgendamentos) {
        this.historicoAgendamentos = historicoAgendamentos;
    }
}
