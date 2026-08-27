package many.studio.web_backend.dto.usuario;

import java.util.List;

public class VisaoGeralClienteResponse {
    private Integer noShow;
    private Double totalGasto;
    private Integer atendimentosPendentes;
    private List<VisaoGeralClienteResponse> visaoGeralClienteResponseList;

    public VisaoGeralClienteResponse(Integer noShow, Double totalGasto, Integer atendimentosPendentes, List<VisaoGeralClienteResponse> visaoGeralClienteResponseList) {
        this.noShow = noShow;
        this.totalGasto = totalGasto;
        this.atendimentosPendentes = atendimentosPendentes;
        this.visaoGeralClienteResponseList = visaoGeralClienteResponseList;
    }

    public VisaoGeralClienteResponse(){}

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

    public Integer getAtendimentosPendentes() {
        return atendimentosPendentes;
    }

    public void setAtendimentosPendentes(Integer atendimentosPendentes) {
        this.atendimentosPendentes = atendimentosPendentes;
    }

    public List<VisaoGeralClienteResponse> getVisaoGeralClienteResponseList() {
        return visaoGeralClienteResponseList;
    }

    public void setVisaoGeralClienteResponseList(List<VisaoGeralClienteResponse> visaoGeralClienteResponseList) {
        this.visaoGeralClienteResponseList = visaoGeralClienteResponseList;
    }
}
