package many.studio.web_backend.service;

import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.dto.pagamento.PagamentoResponse;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.Pagamento;
import many.studio.web_backend.entity.StatusPagamento;
import many.studio.web_backend.entity.TipoPagamento;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.PagamentoRepository;
import many.studio.web_backend.repository.StatusPagamentoRepository;
import many.studio.web_backend.repository.TipoPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusPagamentoRepository statusPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AgendamentoRepository agendamentoRepository,
                            StatusPagamentoRepository statusPagamentoRepository, TipoPagamentoRepository tipoPagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusPagamentoRepository = statusPagamentoRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
    }

    public List<PagamentoResponse> criarSinal(List<Long> idAgendamentos, PagamentoRequest sinal) {
        List<Pagamento> pagamentos = new ArrayList<>();

        for(Long id : idAgendamentos) {

            Agendamento agendamento = agendamentoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Agendameto não encontrado"));

            StatusPagamento status = statusPagamentoRepository.findByEstado("Pago");
            TipoPagamento tipoPagamento = tipoPagamentoRepository.findByTipo("Sinal");


            Pagamento pagamento = new Pagamento();
            pagamento.setValor(sinal.getValor());
            pagamento.setAgendamento(agendamento);
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setStatusPagamento(status);
            Pagamento saved = pagamentoRepository.save(pagamento);
            pagamentos.add(saved);
        }
    }
}
