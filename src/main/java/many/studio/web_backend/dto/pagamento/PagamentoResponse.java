package many.studio.web_backend.dto.pagamento;

import java.time.LocalDateTime;

public class PagamentoResponse {

    private Long id;
    private Double valor;
    private LocalDateTime pagoEm;
    private String comprovanteUrl;
    private AgendamentoDto agendamento;
    private String status;
    private String tipo;

    public static class AgendamentoDto {

        private Long id;
        private String status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getPagoEm() {
        return pagoEm;
    }

    public void setPagoEm(LocalDateTime pagoEm) {
        this.pagoEm = pagoEm;
    }

    public String getComprovanteUrl() {
        return comprovanteUrl;
    }

    public void setComprovanteUrl(String comprovanteUrl) {
        this.comprovanteUrl = comprovanteUrl;
    }

    public AgendamentoDto getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoDto agendamento) {
        this.agendamento = agendamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
