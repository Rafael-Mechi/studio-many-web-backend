package many.studio.web_backend.service;

import org.springframework.stereotype.Service;
import many.studio.web_backend.dto.agendamento.AgendamentoAnaliseResponse;
import many.studio.web_backend.repository.AgendamentoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnaliseAgendamentosService {
    private final AgendamentoRepository agendamentoRepository;

    public AnaliseAgendamentosService(
            AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<AgendamentoAnaliseResponse> buscarPorData(
            LocalDate data) {

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();

        return agendamentoRepository.buscarDadosParaAnalise(
                inicio,
                fim
        );
    }
}
