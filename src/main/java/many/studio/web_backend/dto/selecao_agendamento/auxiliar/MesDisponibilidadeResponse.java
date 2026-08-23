package many.studio.web_backend.dto.selecao_agendamento.auxiliar;

import java.util.List;

public class MesDisponibilidadeResponse {
    private Integer ano;
    private Integer mes;
    private List<DiaDisponibilidadeResponse> dias;

    public MesDisponibilidadeResponse(Integer ano, Integer mes, List<DiaDisponibilidadeResponse> dias) {
        this.ano = ano;
        this.mes = mes;
        this.dias = dias;
    }

    public MesDisponibilidadeResponse(){}

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public List<DiaDisponibilidadeResponse> getDias() {
        return dias;
    }

    public void setDias(List<DiaDisponibilidadeResponse> dias) {
        this.dias = dias;
    }
}
