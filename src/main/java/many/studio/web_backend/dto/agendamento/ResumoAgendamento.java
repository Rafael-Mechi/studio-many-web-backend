package many.studio.web_backend.dto.agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public class ResumoAgendamento {
    private Long idAgendamento;
    private LocalDate dataAgendamento;
    private LocalTime horaAgendamento;
    private Long idServico;
    private String nomeServico;
    private Double precoServico;
    private Integer duracaoServico;
    private String categoriaServico;
    private Long idPacote;
    private String nomePacote;
    private Long idProfissional;
    private String emailProfissional;
    private String telefoneProfissional;

    public ResumoAgendamento(Long idAgendamento, LocalDate dataAgendamento, LocalTime horaAgendamento, Long idServico, String nomeServico, Double precoServico, Integer duracaoServico, String categoriaServico, Long idPacote, String nomePacote, Long idProfissional, String emailProfissional, String telefoneProfissional) {
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
        this.telefoneProfissional = telefoneProfissional;
    }

    public ResumoAgendamento(){}

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
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

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
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

    public Long getIdPacote() {
        return idPacote;
    }

    public void setIdPacote(Long idPacote) {
        this.idPacote = idPacote;
    }

    public String getNomePacote() {
        return nomePacote;
    }

    public void setNomePacote(String nomePacote) {
        this.nomePacote = nomePacote;
    }

    public Long getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(Long idProfissional) {
        this.idProfissional = idProfissional;
    }

    public String getEmailProfissional() {
        return emailProfissional;
    }

    public void setEmailProfissional(String emailProfissional) {
        this.emailProfissional = emailProfissional;
    }

    public String getTelefoneProfissional() {
        return telefoneProfissional;
    }

    public void setTelefoneProfissional(String telefoneProfissional) {
        this.telefoneProfissional = telefoneProfissional;
    }
}
