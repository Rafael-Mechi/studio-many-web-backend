package many.studio.web_backend.service;

import many.studio.web_backend.dto.calendario.DiasDeTrabalhoResponse;
import many.studio.web_backend.dto.calendario.Slot;
import many.studio.web_backend.dto.calendario.SlotsResponse;
import many.studio.web_backend.dto.calendario.StatusSlot;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.AgendamentoItemRepository;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.DiasDeTrabalhoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiasDeTrabalhoService {

    private final DiasDeTrabalhoRepository diasDeTrabalhoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;

    public DiasDeTrabalhoService(DiasDeTrabalhoRepository diasDeTrabalhoRepository, ProfissionalRepository profissionalRepository, AgendamentoRepository agendamentoRepository, AgendamentoItemRepository agendamentoItemRepository) {
        this.diasDeTrabalhoRepository = diasDeTrabalhoRepository;
        this.profissionalRepository = profissionalRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
    }

    public DiasDeTrabalho criar(DiasDeTrabalho diasDeTrabalho) {

        Profissional profissional = profissionalRepository.findById(diasDeTrabalho.getProfissional().getId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        diasDeTrabalho.setProfissional(profissional);

        return diasDeTrabalhoRepository.save(diasDeTrabalho);
    }

    public List<SlotsResponse> gerarDiasDisponiveisPorProfissional(Long profissionalId, YearMonth mes) {

        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        DiasDeTrabalhoResponse.ProfissionalDto profissionalDto = new DiasDeTrabalhoResponse.ProfissionalDto();
        profissionalDto.setId(profissionalId);
        profissionalDto.setNome(profissional.getNome());

        List<DiasDeTrabalho> diasDeTrabalho = diasDeTrabalhoRepository.findByProfissionalId(profissionalId);

        List<DiasDeTrabalhoResponse> diasDeTrabalhoNoMes = new ArrayList<>();
        LocalDate inicio = mes.atDay(1);
        LocalDate fim = mes.atEndOfMonth();

        for(LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {

            DayOfWeek diaSemanaAtual = data.getDayOfWeek();

            for(DiasDeTrabalho dias : diasDeTrabalho) {

                if(dias.getDiaDaSemana() == diaSemanaAtual) {

                    diasDeTrabalhoNoMes.add(
                            new DiasDeTrabalhoResponse(
                                    data,
                                    dias.getHoraInicio(),
                                    dias.getHoraFim(),
                                    profissionalDto
                            )
                    );
                }
            }
        }

        return gerarSlots(diasDeTrabalhoNoMes);
    }

    public List<SlotsResponse> gerarSlots(List<DiasDeTrabalhoResponse> diasDeTrabalhoNoMes) {

        List<SlotsResponse> slotsPorDia = new ArrayList<>();

        List<AgendamentoItem> agendamentosMarcados = agendamentoItemRepository.findByProfissionalId(
                diasDeTrabalhoNoMes.getFirst().getProfissional().getId()
        );

        for(DiasDeTrabalhoResponse dia : diasDeTrabalhoNoMes) {
            List<Slot> slotsDoDia = new ArrayList<>();

            LocalDateTime inicio = LocalDateTime.of(
                    dia.getData(),
                    dia.getInicio()
            );

            LocalDateTime fim = LocalDateTime.of(
                    dia.getData(),
                    dia.getFim()
            );

            LocalDateTime horarioAtual = inicio;

            while(horarioAtual.plusMinutes(30).isBefore(fim) || horarioAtual.plusMinutes(30).equals(fim)) {

                LocalDateTime inicioSlot = horarioAtual;
                LocalDateTime fimSlot = horarioAtual.plusMinutes(30);

                boolean ocupado = agendamentosMarcados.stream()
                                .anyMatch(agendamentoItem ->
                                        possuiConflito(
                                                inicioSlot,
                                                fimSlot,
                                                agendamentoItem
                                        ));

                slotsDoDia.add(new Slot(horarioAtual,
                                        horarioAtual.plusMinutes(30),
                                        ocupado ? StatusSlot.INDISPONIVEL : StatusSlot.DISPONIVEL
                        ));

                horarioAtual = fimSlot;
            }


            slotsPorDia.add(
                    new SlotsResponse(dia.getData(), slotsDoDia)
            );
        }

        return slotsPorDia;
    }

    public boolean possuiConflito(LocalDateTime inicioSlot, LocalDateTime fimSlot, AgendamentoItem agendamentoItem) {
        return inicioSlot.isBefore(agendamentoItem.getFimAtendimento())
                &&
                fimSlot.isAfter(agendamentoItem.getInicioAtendimento());
    }




}
