package many.studio.web_backend.dto.selecao_agendamento.auxiliar;

import java.util.List;

public class MesDisponibilidadeResponse {
    private Integer ano;
    private Integer mes;
    private List<DiaDisponibilidadeResponse> dias;
}
