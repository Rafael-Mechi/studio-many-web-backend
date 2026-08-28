package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.pagamento.PagamentoResponse;
import many.studio.web_backend.entity.Pagamento;

public class PagamentoMapper {

    public static PagamentoResponse toResponse(Pagamento pagamento) {

        PagamentoResponse response = new PagamentoResponse();
        response.setId(pagamento.getId());
        response.setComprovanteUrl(pagamento.getComprovanteUrl());
        response.setPagoEm(pagamento.getPagoEm());
        response.setValor(pagamento.getValor());

        return response;
    }
}
