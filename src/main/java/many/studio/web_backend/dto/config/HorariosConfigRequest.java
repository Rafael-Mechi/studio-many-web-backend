package many.studio.web_backend.dto.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class HorariosConfigRequest {

    private IntervaloDto almoco;

    @NotNull(message = "Jornada é obrigatória")
    @Size(min = 1, max = 7, message = "Jornada deve ter entre 1 e 7 dias")
    @Valid
    private List<HorarioDiaDto> dias;

    public IntervaloDto getAlmoco() {
        return almoco;
    }

    public void setAlmoco(IntervaloDto almoco) {
        this.almoco = almoco;
    }

    public List<HorarioDiaDto> getDias() {
        return dias;
    }

    public void setDias(List<HorarioDiaDto> dias) {
        this.dias = dias;
    }
}
