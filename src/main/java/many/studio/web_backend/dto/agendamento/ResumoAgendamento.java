package many.studio.web_backend.dto.agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResumoAgendamento {
    private Long id;
    private LocalDate data;
    private LocalTime hora;
    private Long servicoId;
    private String servicoNome;
    private Double servicoPreco;
    private Integer servicoDuracao;
    private String categoria;
    private Long pacoteId;
    private String pacoteNome;
    private Long profissionalId;
    private String profissionalEmail;
    private String profissionalTelefone;

    public ResumoAgendamento(
            Long id,
            LocalDateTime inicioAtendimento,
            Long servicoId,
            String servicoNome,
            Double servicoPreco,
            Integer servicoDuracao,
            String categoria,
            Long pacoteId,
            String pacoteNome,
            Long profissionalId,
            String profissionalEmail,
            String profissionalTelefone
    ) {
        this.id = id;
        this.data = inicioAtendimento.toLocalDate();
        this.hora = inicioAtendimento.toLocalTime();
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.servicoPreco = servicoPreco;
        this.servicoDuracao = servicoDuracao;
        this.categoria = categoria;
        this.pacoteId = pacoteId;
        this.pacoteNome = pacoteNome;
        this.profissionalId = profissionalId;
        this.profissionalEmail = profissionalEmail;
        this.profissionalTelefone = profissionalTelefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public Double getServicoPreco() {
        return servicoPreco;
    }

    public void setServicoPreco(Double servicoPreco) {
        this.servicoPreco = servicoPreco;
    }

    public Integer getServicoDuracao() {
        return servicoDuracao;
    }

    public void setServicoDuracao(Integer servicoDuracao) {
        this.servicoDuracao = servicoDuracao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getPacoteId() {
        return pacoteId;
    }

    public void setPacoteId(Long pacoteId) {
        this.pacoteId = pacoteId;
    }

    public String getPacoteNome() {
        return pacoteNome;
    }

    public void setPacoteNome(String pacoteNome) {
        this.pacoteNome = pacoteNome;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public String getProfissionalEmail() {
        return profissionalEmail;
    }

    public void setProfissionalEmail(String profissionalEmail) {
        this.profissionalEmail = profissionalEmail;
    }

    public String getProfissionalTelefone() {
        return profissionalTelefone;
    }

    public void setProfissionalTelefone(String profissionalTelefone) {
        this.profissionalTelefone = profissionalTelefone;
    }
}
