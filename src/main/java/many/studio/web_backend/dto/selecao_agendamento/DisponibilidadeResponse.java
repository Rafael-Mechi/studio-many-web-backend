package many.studio.web_backend.dto.selecao_agendamento;

import many.studio.web_backend.dto.selecao_agendamento.auxiliar.FuncionarioDisponibilidadeResponse;

import java.util.List;

public class DisponibilidadeResponse {
    List<FuncionarioDisponibilidadeResponse> funcionarios;

    public DisponibilidadeResponse(List<FuncionarioDisponibilidadeResponse> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public DisponibilidadeResponse(){}

    public List<FuncionarioDisponibilidadeResponse> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<FuncionarioDisponibilidadeResponse> funcionarios) {
        this.funcionarios = funcionarios;
    }
}
