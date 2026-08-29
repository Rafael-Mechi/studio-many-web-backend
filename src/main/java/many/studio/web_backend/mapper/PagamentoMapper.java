package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.pagamento.PagamentoResponse;
import many.studio.web_backend.entity.Pagamento;

import java.util.List;

public class PagamentoMapper {

    public static PagamentoResponse toResponse(Pagamento pagamento) {

        PagamentoResponse.AgendamentoDto agendamento = new PagamentoResponse.AgendamentoDto();
        agendamento.setId(pagamento.getAgendamento().getId());
        agendamento.setStatus(pagamento.getAgendamento().getStatusAgendamento().getEstado());

        PagamentoResponse response = new PagamentoResponse();
        response.setId(pagamento.getId());
        response.setComprovanteUrl(pagamento.getComprovanteUrl());
        response.setPagoEm(pagamento.getPagoEm());
        response.setValor(pagamento.getValor());
        response.setAgendamento(agendamento);
        response.setStatus(pagamento.getStatusPagamento().getEstado());
        response.setTipo(pagamento.getTipoPagamento().getTipo());

        return response;
    }

    public static List<PagamentoResponse> toResponseList(List<Pagamento> pagamentos) {
        return pagamentos.stream()
                .map(PagamentoMapper::toResponse)
                .toList();
    }
}
