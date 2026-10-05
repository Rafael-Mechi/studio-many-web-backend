package many.studio.web_backend.dto.usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HistoricoAgendamentos {
    private String servico;
    private LocalDateTime dataHora;
    private String funcionario;
    private String status;
    private Double valor;

    public HistoricoAgendamentos(){}

    public HistoricoAgendamentos(String servico, LocalDateTime dataHora, String funcionario, String status, Double valor) {
        this.servico = servico;
        this.dataHora = dataHora;
        this.funcionario = funcionario;
        this.status = status;
        this.valor = valor;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
