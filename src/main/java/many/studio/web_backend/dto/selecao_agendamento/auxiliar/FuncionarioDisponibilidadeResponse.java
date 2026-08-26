package many.studio.web_backend.dto.selecao_agendamento.auxiliar;

import java.util.List;

public class FuncionarioDisponibilidadeResponse {
    private Long funcionarioId;
    private String nome;
    private List<MesDisponibilidadeResponse> meses;

    public FuncionarioDisponibilidadeResponse(Long funcionarioId, String nome, List<MesDisponibilidadeResponse> meses) {
        this.funcionarioId = funcionarioId;
        this.nome = nome;
        this.meses = meses;
    }

    public FuncionarioDisponibilidadeResponse(){}

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<MesDisponibilidadeResponse> getMeses() {
        return meses;
    }

    public void setMeses(List<MesDisponibilidadeResponse> meses) {
        this.meses = meses;
    }
}
