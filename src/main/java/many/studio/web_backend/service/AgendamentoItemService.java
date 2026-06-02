package many.studio.web_backend.service;

import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.AgendamentoItemRepository;
import many.studio.web_backend.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgendamentoItemService {

    private final AgendamentoItemRepository agendamentoItemRepository;

    public AgendamentoItemService(AgendamentoItemRepository agendamentoItemRepository) {
        this.agendamentoItemRepository = agendamentoItemRepository;
    }

    public AgendamentoItem reagendar(Long id, LocalDateTime novaData) {
        AgendamentoItem item = agendamentoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

        Agendamento agendamento = item.getAgendamento();
        LocalDateTime fimAtendimento = novaData.plusMinutes(agendamento.getPacote().getServico().getDuracaoMinutos());

        item.setInicioAtendimento(novaData);
        item.setFimAtendimento(fimAtendimento);

        return agendamentoItemRepository.save(item);
    }
}
