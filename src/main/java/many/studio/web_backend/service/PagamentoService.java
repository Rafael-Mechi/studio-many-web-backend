package many.studio.web_backend.service;

import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final StatusPagamentoRepository statusPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final ComprovanteRepository comprovanteRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AgendamentoRepository agendamentoRepository,
                            StatusAgendamentoRepository statusAgendamentoRepository,
                            StatusPagamentoRepository statusPagamentoRepository, TipoPagamentoRepository tipoPagamentoRepository, ComprovanteRepository comprovanteRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.statusPagamentoRepository = statusPagamentoRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
        this.comprovanteRepository = comprovanteRepository;
    }

    public List<Pagamento> criarSinal(
            List<Long> idAgendamentos,
            MultipartFile comprovante) {

        List<Pagamento> pagamentos = new ArrayList<>();

        StatusAgendamento statusAgendamento =
                statusAgendamentoRepository
                        .findByEstado("solicitar confirmacao agendamento")
                        .get();

        StatusPagamento status =
                statusPagamentoRepository.findByEstado("pago");

        TipoPagamento tipoPagamento =
                tipoPagamentoRepository.findByTipo("sinal");

        String arquivoBase64;

        try {
            arquivoBase64 = Base64.getEncoder()
                    .encodeToString(comprovante.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler comprovante", e);
        }

        for (Long id : idAgendamentos) {

            Agendamento agendamento = agendamentoRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Agendamento não encontrado"));

            Pagamento pagamento = new Pagamento();

            pagamento.setValor(
                    agendamento.getPacote()
                            .getServico()
                            .getSinalValor()
            );

            pagamento.setAgendamento(agendamento);
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setStatusPagamento(status);

            Pagamento saved = pagamentoRepository.save(pagamento);

            Comprovante comprovanteEntity = new Comprovante();

            comprovanteEntity.setArquivo(arquivoBase64);
            comprovanteEntity.setTipoArquivo(comprovante.getContentType());
            comprovanteEntity.setPagamento(saved);

            comprovanteRepository.save(comprovanteEntity);

            pagamentos.add(saved);

            agendamento.setStatusAgendamento(statusAgendamento);
            agendamentoRepository.save(agendamento);
        }

        return pagamentos;
    }
}
