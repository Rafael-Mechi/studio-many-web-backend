package many.studio.web_backend.service;

import many.studio.web_backend.dto.config.AgendamentoAfetadoDto;
import many.studio.web_backend.dto.config.BloqueioCriacaoResponse;
import many.studio.web_backend.dto.config.BloqueioRequest;
import many.studio.web_backend.dto.config.BloqueioResponse;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.Bloqueio;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.config.BloqueioMapper;
import many.studio.web_backend.repository.AgendamentoItemRepository;
import many.studio.web_backend.repository.BloqueioRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BloqueioService {

    private final BloqueioRepository bloqueioRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;

    public BloqueioService(BloqueioRepository bloqueioRepository,
                           ProfissionalRepository profissionalRepository,
                           AgendamentoItemRepository agendamentoItemRepository) {
        this.bloqueioRepository = bloqueioRepository;
        this.profissionalRepository = profissionalRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
    }

    public List<BloqueioResponse> listar(Long usuarioId) {
        Profissional profissional = profissionalDoUsuario(usuarioId);

        return BloqueioMapper.toResponseList(
                bloqueioRepository.findByProfissionalIdOrderByInicioDesc(profissional.getId()));
    }

    @Transactional
    public BloqueioCriacaoResponse criar(Long usuarioId, BloqueioRequest request) {
        Profissional profissional = profissionalDoUsuario(usuarioId);

        if (request.getData() == null || request.getHoraInicio() == null || request.getHoraFim() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data, início e fim são obrigatórios");
        }
        if (!request.getHoraInicio().isBefore(request.getHoraFim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Início deve ser anterior ao fim");
        }
        String motivo = request.getMotivo() != null ? request.getMotivo().trim() : "";
        if (motivo.length() < 3 || motivo.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo deve ter entre 3 e 255 caracteres");
        }

        LocalDateTime inicio = LocalDateTime.of(request.getData(), request.getHoraInicio());
        LocalDateTime fim = LocalDateTime.of(request.getData(), request.getHoraFim());
        if (!fim.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bloqueio deve ser para um período futuro");
        }
        if (request.getData().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data do bloqueio não pode estar no passado");
        }

        List<AgendamentoAfetadoDto> afetados = detectarConflitos(profissional.getId(), inicio, fim);

        Bloqueio bloqueio = new Bloqueio(null, inicio, fim, motivo, profissional);
        bloqueio = bloqueioRepository.save(bloqueio);

        return BloqueioMapper.toCriacaoResponse(bloqueio, afetados);
    }

    @Transactional
    public void excluir(Long usuarioId, Long bloqueioId) {
        Bloqueio bloqueio = bloqueioRepository.findById(bloqueioId)
                .orElseThrow(() -> new EntityNotFoundException("Bloqueio não encontrado"));

        // 404 (e não 403) de propósito: não revelar a existência de bloqueios de terceiros.
        if (bloqueio.getProfissional() == null
                || bloqueio.getProfissional().getUsuario() == null
                || !usuarioId.equals(bloqueio.getProfissional().getUsuario().getId())) {
            throw new EntityNotFoundException("Bloqueio não encontrado");
        }

        bloqueioRepository.deleteById(bloqueioId);
    }

    private List<AgendamentoAfetadoDto> detectarConflitos(Long profissionalId,
                                                          LocalDateTime inicio,
                                                          LocalDateTime fim) {
        List<AgendamentoAfetadoDto> afetados = new ArrayList<>();
        List<AgendamentoItem> ocupantes =
                agendamentoItemRepository.findAgendamentosQueOcupamHorario(profissionalId);

        for (AgendamentoItem item : ocupantes) {
            if (item.getInicioAtendimento() == null || item.getFimAtendimento() == null) {
                continue;
            }
            boolean conflita = inicio.isBefore(item.getFimAtendimento())
                    && fim.isAfter(item.getInicioAtendimento());
            if (conflita && item.getAgendamento() != null) {
                afetados.add(BloqueioMapper.toAgendamentoAfetadoDto(item));
            }
        }
        return afetados;
    }

    private Profissional profissionalDoUsuario(Long usuarioId) {
        return profissionalRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
    }
}
