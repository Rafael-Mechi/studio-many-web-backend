package many.studio.web_backend.dto.selecao_agendamento.auxiliar;

import java.util.List;

public class FuncionarioDisponibilidadeResponse {
    private Long funcionarioId;
    private String nome;
    private List<MesDisponibilidadeResponse> meses;
}
