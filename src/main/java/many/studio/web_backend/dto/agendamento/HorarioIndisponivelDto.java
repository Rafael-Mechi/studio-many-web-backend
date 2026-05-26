package many.studio.web_backend.dto.agendamento;

import java.time.LocalDateTime;

public class HorarioIndisponivelDto {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String nomeProfissional;
    private String nomeServico;

    public HorarioIndisponivelDto(LocalDateTime dataInicio, LocalDateTime dataFim, String nomeProfissional, String nomeServico) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.nomeProfissional = nomeProfissional;
        this.nomeServico = nomeServico;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }
}