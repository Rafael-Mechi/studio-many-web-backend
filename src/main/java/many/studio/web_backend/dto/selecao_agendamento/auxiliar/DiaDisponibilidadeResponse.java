package many.studio.web_backend.dto.selecao_agendamento.auxiliar;

import java.time.LocalTime;
import java.util.List;

public class DiaDisponibilidadeResponse {
    private Integer dia;
    private List<LocalTime> horarios;

    public DiaDisponibilidadeResponse(){}

    public DiaDisponibilidadeResponse(Integer dia, List<LocalTime> horarios) {
        this.dia = dia;
        this.horarios = horarios;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public List<LocalTime> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<LocalTime> horarios) {
        this.horarios = horarios;
    }
}
