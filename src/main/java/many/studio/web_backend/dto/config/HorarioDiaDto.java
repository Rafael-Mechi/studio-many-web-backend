package many.studio.web_backend.dto.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class HorarioDiaDto {

    @NotNull(message = "Dia da semana é obrigatório")
    private DayOfWeek diaSemana;

    @NotNull(message = "Ativo é obrigatório")
    private Boolean ativo;

    @Valid
    private List<HorarioServicoDto> servicos = new ArrayList<>();

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<HorarioServicoDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<HorarioServicoDto> servicos) {
        this.servicos = servicos != null ? servicos : new ArrayList<>();
    }
}
