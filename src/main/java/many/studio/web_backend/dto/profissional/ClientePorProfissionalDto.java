package many.studio.web_backend.dto.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ClientePorProfissionalDto {
    @Schema(description = "ID do profissional", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do profissional", example = "Carlos")
    private String nomeFuncionario;

    @NotNull
    @Schema(description = "ID do cliente", example = "1")
    private Long clienteId;

    @NotBlank
    @Schema(description = "Nome do cliente", example = "Giovana Lopes")
    private String nomeCliente;

    @NotBlank
    @Email
    @Schema(description = "E-mail do cliente", example = "giovana.lopes@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Telefone do cliente", example = "(11) 91234-1234")
    private String telefone;

    @Schema(description = "Serviço preferido do cliente", example = "Depilação a laser")
    private String servicoPreferido;

    @Past
    @Schema(description = "Data do último agendamento do cliente", example = "2026-05-10")
    private LocalDate ultimaVisita;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de no-shows", example = "0")
    private Integer noShow;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total gasto pelo cliente", example = "70.00")
    private Double totalGasto;

    public ClientePorProfissionalDto() {
    }

    public ClientePorProfissionalDto(Long id, String nomeFuncionario, Long clienteId, String nomeCliente, String email, String telefone, String servicoPreferido, LocalDate ultimaVisita, Integer noShow, Double totalGasto) {
        this.id = id;
        this.nomeFuncionario = nomeFuncionario;
        this.clienteId = clienteId;
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.telefone = telefone;
        this.servicoPreferido = servicoPreferido;
        this.ultimaVisita = ultimaVisita;
        this.noShow = noShow;
        this.totalGasto = totalGasto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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

    public String getServicoPreferido() {
        return servicoPreferido;
    }

    public void setServicoPreferido(String servicoPreferido) {
        this.servicoPreferido = servicoPreferido;
    }

    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
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
}
