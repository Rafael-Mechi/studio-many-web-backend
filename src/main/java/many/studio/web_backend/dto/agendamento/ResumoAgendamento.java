package many.studio.web_backend.dto.agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public class ResumoAgendamento {
    private Integer idAgendamento;
    private LocalDate dataAgendamento;
    private LocalTime horaAgendamento;
    private Integer idServico;
    private String nomeServico;
    private Double precoServico;
    private Integer duracaoServico;
    private String categoriaServico;
    private Integer idPacote;
    private String nomePacote;
    private Integer idProfissional;
    private String emailProfissional;
    private String telefone;

    public ResumoAgendamento(Integer idAgendamento, LocalDate dataAgendamento, LocalTime horaAgendamento, Integer idServico, String nomeServico, Double precoServico, Integer duracaoServico, String categoriaServico, Integer idPacote, String nomePacote, Integer idProfissional, String emailProfissional, String telefone) {
        this.idAgendamento = idAgendamento;
        this.dataAgendamento = dataAgendamento;
        this.horaAgendamento = horaAgendamento;
        this.idServico = idServico;
        this.nomeServico = nomeServico;
        this.precoServico = precoServico;
        this.duracaoServico = duracaoServico;
        this.categoriaServico = categoriaServico;
        this.idPacote = idPacote;
        this.nomePacote = nomePacote;
        this.idProfissional = idProfissional;
        this.emailProfissional = emailProfissional;
        this.telefone = telefone;
    }

    public ResumoAgendamento(){}

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalTime getHoraAgendamento() {
        return horaAgendamento;
    }

    public void setHoraAgendamento(LocalTime horaAgendamento) {
        this.horaAgendamento = horaAgendamento;
    }

    public Integer getIdServico() {
        return idServico;
    }

    public void setIdServico(Integer idServico) {
        this.idServico = idServico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public Double getPrecoServico() {
        return precoServico;
    }

    public void setPrecoServico(Double precoServico) {
        this.precoServico = precoServico;
    }

    public Integer getDuracaoServico() {
        return duracaoServico;
    }

    public void setDuracaoServico(Integer duracaoServico) {
        this.duracaoServico = duracaoServico;
    }

    public String getCategoriaServico() {
        return categoriaServico;
    }

    public void setCategoriaServico(String categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public Integer getIdPacote() {
        return idPacote;
    }

    public void setIdPacote(Integer idPacote) {
        this.idPacote = idPacote;
    }

    public String getNomePacote() {
        return nomePacote;
    }

    public void setNomePacote(String nomePacote) {
        this.nomePacote = nomePacote;
    }

    public Integer getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(Integer idProfissional) {
        this.idProfissional = idProfissional;
    }

    public String getEmailProfissional() {
        return emailProfissional;
    }

    public void setEmailProfissional(String emailProfissional) {
        this.emailProfissional = emailProfissional;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
