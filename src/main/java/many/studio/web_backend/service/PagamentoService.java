package many.studio.web_backend.service;

import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final StatusPagamentoRepository statusPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AgendamentoRepository agendamentoRepository,
                            StatusAgendamentoRepository statusAgendamentoRepository,
                            StatusPagamentoRepository statusPagamentoRepository, TipoPagamentoRepository tipoPagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.statusPagamentoRepository = statusPagamentoRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
    }

    public List<Pagamento> criarSinal(List<Long> idAgendamentos) {
        List<Pagamento> pagamentos = new ArrayList<>();

        StatusAgendamento statusAgendamento = statusAgendamentoRepository.findByEstado("solicitar confirmacao agendamento").get();

        for(Long id : idAgendamentos) {

            Agendamento agendamento = agendamentoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Agendameto não encontrado"));

            StatusPagamento status = statusPagamentoRepository.findByEstado("pago");
            TipoPagamento tipoPagamento = tipoPagamentoRepository.findByTipo("sinal");


            Pagamento pagamento = new Pagamento();
            pagamento.setValor(agendamento.getPacote().getServico().getSinalValor());
            pagamento.setAgendamento(agendamento);
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setStatusPagamento(status);
            Pagamento saved = pagamentoRepository.save(pagamento);
            pagamentos.add(saved);

            agendamento.setStatusAgendamento(statusAgendamento);
            agendamentoRepository.save(agendamento);
        }

        return pagamentos;
    }
}
