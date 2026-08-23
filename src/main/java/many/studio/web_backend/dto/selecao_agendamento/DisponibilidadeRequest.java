package many.studio.web_backend.dto.selecao_agendamento;

import java.util.List;

public class DisponibilidadeRequest {
    private Long idServico;
    private List<Long> idsProfissionais;

    public DisponibilidadeRequest(Long idServico, List<Long> idsProfissionais) {
        this.idServico = idServico;
        this.idsProfissionais = idsProfissionais;
    }

    public DisponibilidadeRequest(){}

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public List<Long> getIdsProfissionais() {
        return idsProfissionais;
    }

    public void setIdsProfissionais(List<Long> idProfissionais) {
        this.idsProfissionais = idProfissionais;
    }
}
