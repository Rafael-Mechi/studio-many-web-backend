package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.StatusAgendamento;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.repository.AgendamentoItemRepository;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.StatusAgendamentoRepository;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AgendamentoItemService {

    private final AgendamentoItemRepository agendamentoItemRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final AgendamentoHelper agendamentoHelper;
    private final NotificacaoAgendamentoService notificacaoAgendamentoService;

    public AgendamentoItemService(
            AgendamentoItemRepository agendamentoItemRepository,
            AgendamentoRepository agendamentoRepository,
            StatusAgendamentoRepository statusAgendamentoRepository,
            AgendamentoHelper agendamentoHelper,
            NotificacaoAgendamentoService notificacaoAgendamentoService
    ) {
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.agendamentoHelper = agendamentoHelper;
        this.notificacaoAgendamentoService = notificacaoAgendamentoService;
    }

    @Transactional
    public AgendamentoItem reagendar(
            Long id,
            LocalDateTime novaData,
            UsuarioDetalhesDto usuarioLogado
    ) {
        if (novaData == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A nova data e horário são obrigatórios"
            );
        }

        AgendamentoItem item = agendamentoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

        Agendamento agendamento = item.getAgendamento();
        if (!agendamentoHelper.isUsuarioValidoParaAlterarAgendamento(
                usuarioLogado.getId(),
                agendamento
        )) {
            throw new NonAuthorizedException("Você não pode reagendar este agendamento");
        }

        String estado = agendamento.getStatusAgendamento() == null
                || agendamento.getStatusAgendamento().getEstado() == null
                ? ""
                : agendamento.getStatusAgendamento().getEstado().trim().toLowerCase();

        if (Set.of(
                StatusAgendamento.CONCLUIDO,
                StatusAgendamento.CANCELADO,
                StatusAgendamento.RECUSADO,
                StatusAgendamento.NO_SHOW
        ).contains(estado)) {
            throw new EntityConflictException(
                    "Agendamentos com status '" + estado + "' não podem ser reagendados"
            );
        }

        if (novaData.isBefore(LocalDateTime.now())) {
            throw new EntityConflictException("A nova data e horário devem estar no futuro");
        }

        LocalDateTime fimAtendimento = novaData.plusMinutes(
                agendamento.getPacote().getServico().getDuracaoMinutos()
        );

        agendamentoHelper.validarConflitoHorarioItem(
                novaData,
                agendamento.getPacote().getServico().getDuracaoMinutos(),
                agendamento.getProfissional().getId(),
                agendamento.getId()
        );

        item.setInicioAtendimento(novaData);
        item.setFimAtendimento(fimAtendimento);

        agendamento.setQtdRemarcacoes(
                agendamento.getQtdRemarcacoes() == null
                        ? 1
                        : agendamento.getQtdRemarcacoes() + 1
        );

        StatusAgendamento statusReagendado = statusAgendamentoRepository
                .findByEstado(StatusAgendamento.REAGENDADO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Status REAGENDADO não encontrado"
                ));

        agendamento.setStatusAgendamento(statusReagendado);
        AgendamentoItem itemSalvo = agendamentoItemRepository.save(item);
        agendamentoRepository.save(agendamento);

        notificacaoAgendamentoService.notificarReagendamento(agendamento, itemSalvo);

        return itemSalvo;
    }
}
