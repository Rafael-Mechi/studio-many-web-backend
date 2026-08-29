package many.studio.web_backend.dto.usuario;

import many.studio.web_backend.dto.agendamento.ResumoAgendamento;

import java.util.List;

public class VisaoGeralClienteResponse {
    private String nomeUsuario;
    private String emailUsuario;
    private Integer noShow;
    private Double totalGasto;
    private Integer atendimentosPendentes;
    private List<ResumoAgendamento> resumoAgendamentos;

    public VisaoGeralClienteResponse(String nomeUsuario, String emailUsuario, Integer noShow, Double totalGasto, Integer atendimentosPendentes, List<ResumoAgendamento> resumoAgendamentos) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.noShow = noShow;
        this.totalGasto = totalGasto;
        this.atendimentosPendentes = atendimentosPendentes;
        this.resumoAgendamentos = resumoAgendamentos;
    }

    public VisaoGeralClienteResponse(){
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Integer getNoShow() {
        return noShow;
    }

    public void setNoShow(Integer noShow) {
        this.noShow = noShow;
    }

    public Double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(Double totalGasto) {
        this.totalGasto = totalGasto;
    }

    public Integer getAtendimentosPendentes() {
        return atendimentosPendentes;
    }

    public void setAtendimentosPendentes(Integer atendimentosPendentes) {
        this.atendimentosPendentes = atendimentosPendentes;
    }

    public List<ResumoAgendamento> getResumoAgendamentos() {
        return resumoAgendamentos;
    }

    public void setResumoAgendamentos(List<ResumoAgendamento> resumoAgendamentos) {
        this.resumoAgendamentos = resumoAgendamentos;
    }
}
