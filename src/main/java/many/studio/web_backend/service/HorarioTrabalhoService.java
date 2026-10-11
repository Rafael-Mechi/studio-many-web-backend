package many.studio.web_backend.service;

import many.studio.web_backend.dto.config.HorarioDiaDto;
import many.studio.web_backend.dto.config.HorarioServicoDto;
import many.studio.web_backend.dto.config.HorariosConfigRequest;
import many.studio.web_backend.dto.config.IntervaloDto;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.config.HorarioTrabalhoMapper;
import many.studio.web_backend.repository.DiasDeTrabalhoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class HorarioTrabalhoService {

    private static final List<DayOfWeek> SEMANA = HorarioTrabalhoMapper.semanaOrdenada();

    private final DiasDeTrabalhoRepository diasDeTrabalhoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;

    public HorarioTrabalhoService(DiasDeTrabalhoRepository diasDeTrabalhoRepository,
                                  ProfissionalRepository profissionalRepository,
                                  ServicoRepository servicoRepository,
                                  ServicoProfissionalRepository servicoProfissionalRepository) {
        this.diasDeTrabalhoRepository = diasDeTrabalhoRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
    }

    public HorariosConfigRequest listar(Long usuarioId) {
        Profissional profissional = profissionalDoUsuario(usuarioId);

        List<DiasDeTrabalho> linhas =
                diasDeTrabalhoRepository.findByProfissionalId(profissional.getId());

        return HorarioTrabalhoMapper.toResponse(linhas, profissional);
    }

    @Transactional
    public HorariosConfigRequest salvar(Long usuarioId, HorariosConfigRequest request) {
        Profissional profissional = profissionalDoUsuario(usuarioId);

        validarAlmoco(request.getAlmoco());
        if (request.getDias() == null || request.getDias().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jornada é obrigatória");
        }

        Set<DayOfWeek> diasVistos = new HashSet<>();
        Set<Long> servicosIds = new HashSet<>();
        for (HorarioDiaDto dia : request.getDias()) {
            if (dia.getDiaSemana() == null || dia.getAtivo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dia da semana e ativo são obrigatórios");
            }
            if (!diasVistos.add(dia.getDiaSemana())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dia da semana duplicado na jornada");
            }
            List<HorarioServicoDto> itens = dia.getServicos() != null ? dia.getServicos() : List.of();
            if (Boolean.TRUE.equals(dia.getAtivo()) && itens.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Dia ativo precisa de ao menos um horário com serviço");
            }
            if (Boolean.FALSE.equals(dia.getAtivo()) && !itens.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Dia inativo (folga) não pode ter horários");
            }
            Set<Long> servicosNoDia = new HashSet<>();
            for (HorarioServicoDto item : itens) {
                if (item.getServicoId() == null || item.getInicio() == null || item.getFim() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Serviço, início e fim são obrigatórios em cada horário");
                }
                if (!item.getInicio().isBefore(item.getFim())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Início deve ser anterior ao fim em cada horário");
                }
                if (!servicosNoDia.add(item.getServicoId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Serviço duplicado no mesmo dia");
                }
                servicosIds.add(item.getServicoId());
            }
        }

        Map<Long, Servico> servicos = new HashMap<>();
        if (!servicosIds.isEmpty()) {
            servicoRepository.findAllById(servicosIds).forEach(s -> servicos.put(s.getId(), s));
            for (Long id : servicosIds) {
                if (!servicos.containsKey(id)) {
                    throw new EntityNotFoundException("Serviço não encontrado");
                }
            }
        }

        diasDeTrabalhoRepository.deleteByProfissionalId(profissional.getId());

        for (HorarioDiaDto dia : request.getDias()) {
            if (Boolean.FALSE.equals(dia.getAtivo())) {
                continue;
            }
            for (HorarioServicoDto item : dia.getServicos()) {
                diasDeTrabalhoRepository.save(HorarioTrabalhoMapper.toEntity(
                        dia.getDiaSemana(), item, profissional, servicos.get(item.getServicoId())));
            }
        }

        if (request.getAlmoco() != null) {
            profissional.setAlmocoInicio(request.getAlmoco().getInicio());
            profissional.setAlmocoFim(request.getAlmoco().getFim());
        } else {
            profissional.setAlmocoInicio(null);
            profissional.setAlmocoFim(null);
        }
        profissionalRepository.save(profissional);

        sincronizarVinculosServico(profissional, servicosIds);

        return listar(usuarioId);
    }

    private void validarAlmoco(IntervaloDto almoco) {
        if (almoco == null) {
            return;
        }
        if ((almoco.getInicio() == null) != (almoco.getFim() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Intervalo de almoço precisa de início e fim");
        }
        if (almoco.getInicio() != null && !almoco.getInicio().isBefore(almoco.getFim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Início do almoço deve ser anterior ao fim");
        }
    }

    private void sincronizarVinculosServico(Profissional profissional, Set<Long> servicosIds) {
        for (Long servicoId : servicosIds) {
            if (!servicoProfissionalRepository.existsByProfissionalIdAndServicoId(
                    profissional.getId(), servicoId)) {
                Servico servico = servicoRepository.findById(servicoId)
                        .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));
                servicoProfissionalRepository.save(new ServicoProfissional(servico, profissional));
            }
        }
    }

    private Profissional profissionalDoUsuario(Long usuarioId) {
        return profissionalRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
    }
}
