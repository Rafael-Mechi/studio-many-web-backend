package many.studio.web_backend.service;

import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeRequest;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.DiaDisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.FuncionarioDisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.MesDisponibilidadeResponse;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadeService {
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final DiasDeTrabalhoRepository diaDeTrabalhoRepository;
    private final BloqueioRepository bloqueioRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;

    private static final int INTERVALO_SLOT_MINUTOS = 15;

    public DisponibilidadeService(ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository, DiasDeTrabalhoRepository diaDeTrabalhoRepository, BloqueioRepository bloqueioRepository, AgendamentoItemRepository agendamentoItemRepository) {
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.diaDeTrabalhoRepository = diaDeTrabalhoRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
    }

    public DisponibilidadeResponse calcular(
            DisponibilidadeRequest request
    ) {

        Servico servico = servicoRepository
                .findById(request.getIdServico())
                .orElseThrow(() ->
                        new EntityNotFoundException("Serviço não encontrado")
                );

        List<Profissional> profissionais =
                profissionalRepository.findAllById(
                        request.getIdsProfissionais()
                );

        List<FuncionarioDisponibilidadeResponse> funcionarios =
                new ArrayList<>();

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusMonths(3);

        for (Profissional profissional : profissionais) {

            List<DiasDeTrabalho> diasDeTrabalho =
                    diaDeTrabalhoRepository
                            .findByProfissionalId(profissional.getId());

            List<Bloqueio> bloqueios =
                    bloqueioRepository
                            .findByProfissionalId(profissional.getId());

            List<AgendamentoItem> agendamentos =
                    agendamentoItemRepository
                            .findAgendamentosQueOcupamHorario(
                                    profissional.getId()
                            );

            List<MesDisponibilidadeResponse> meses =
                    new ArrayList<>();

            LocalDate dataAtual = hoje;

            while (!dataAtual.isAfter(limite)) {

                LocalDate finalDataAtual = dataAtual;
                DiasDeTrabalho diaDeTrabalho =
                        diasDeTrabalho.stream()
                                .filter(dia ->
                                        dia.getDiaDaSemana()
                                                .equals(finalDataAtual.getDayOfWeek())
                                )
                                .findFirst()
                                .orElse(null);

                if (diaDeTrabalho != null) {

                    // 1. GERA TODOS OS SLOTS POSSÍVEIS
                    List<LocalTime> horarios =
                            gerarSlots(
                                    diaDeTrabalho.getHoraInicio(),
                                    diaDeTrabalho.getHoraFim(),
                                    servico.getDuracaoMinutos()
                            );


                    // 2. REMOVE HORÁRIOS QUE CONFLITAM COM BLOQUEIOS
                    List<LocalTime> horariosDisponiveis =
                            horarios.stream()
                                    .filter(horario -> {

                                        LocalDateTime inicioSlot =
                                                LocalDateTime.of(
                                                        finalDataAtual,
                                                        horario
                                                );

                                        LocalDateTime fimSlot =
                                                inicioSlot.plusMinutes(
                                                        servico.getDuracaoMinutos()
                                                );

                                        return !estaBloqueado(
                                                inicioSlot,
                                                fimSlot,
                                                bloqueios
                                        );
                                    })
                                    .toList();


                    // 3. REMOVE HORÁRIOS QUE CONFLITAM COM AGENDAMENTOS
                    horariosDisponiveis =
                            horariosDisponiveis.stream()
                                    .filter(horario -> {

                                        LocalDateTime inicioSlot =
                                                LocalDateTime.of(
                                                        finalDataAtual,
                                                        horario
                                                );

                                        LocalDateTime fimSlot =
                                                inicioSlot.plusMinutes(
                                                        servico.getDuracaoMinutos()
                                                );

                                        return !temConflitoComAgendamento(
                                                inicioSlot,
                                                fimSlot,
                                                agendamentos
                                        );
                                    })
                                    .toList();


                    // 4. SE NÃO SOBROU NENHUM HORÁRIO,
                    // NÃO ADICIONA O DIA
                    if (!horariosDisponiveis.isEmpty()) {

                        MesDisponibilidadeResponse mesResponse =
                                meses.stream()
                                        .filter(mes ->
                                                mes.getAno().equals(
                                                        finalDataAtual.getYear()
                                                )
                                                        &&
                                                        mes.getMes().equals(
                                                                finalDataAtual.getMonthValue()
                                                        )
                                        )
                                        .findFirst()
                                        .orElseGet(() -> {

                                            MesDisponibilidadeResponse novoMes =
                                                    new MesDisponibilidadeResponse();

                                            novoMes.setAno(
                                                    finalDataAtual.getYear()
                                            );

                                            novoMes.setMes(
                                                    finalDataAtual.getMonthValue()
                                            );

                                            novoMes.setDias(
                                                    new ArrayList<>()
                                            );

                                            meses.add(novoMes);

                                            return novoMes;
                                        });


                        // 5. AGORA SIM CRIA O DIA
                        DiaDisponibilidadeResponse diaResponse =
                                new DiaDisponibilidadeResponse();

                        diaResponse.setDia(
                                dataAtual.getDayOfMonth()
                        );

                        diaResponse.setHorarios(
                                horariosDisponiveis
                        );

                        mesResponse.getDias()
                                .add(diaResponse);
                    }
                }

                dataAtual = dataAtual.plusDays(1);
            }

            FuncionarioDisponibilidadeResponse funcionarioResponse =
                    new FuncionarioDisponibilidadeResponse();

            funcionarioResponse.setFuncionarioId(profissional.getId());
            funcionarioResponse.setNome(profissional.getNome());
            funcionarioResponse.setMeses(meses);

            funcionarios.add(funcionarioResponse);
        }

        DisponibilidadeResponse response =
                new DisponibilidadeResponse();

        response.setFuncionarios(funcionarios);

        return response;
    }

    private List<LocalTime> gerarSlots(
            LocalTime horaInicio,
            LocalTime horaFim,
            Integer duracaoServico
    ) {

        List<LocalTime> horarios =
                new ArrayList<>();

        LocalTime horarioAtual = horaInicio;

        while (!horarioAtual
                .plusMinutes(duracaoServico)
                .isAfter(horaFim)) {

            horarios.add(horarioAtual);

            horarioAtual =
                    horarioAtual.plusMinutes(INTERVALO_SLOT_MINUTOS);
        }

        return horarios;
    }

    private boolean estaBloqueado(
            LocalDateTime inicioSlot,
            LocalDateTime fimSlot,
            List<Bloqueio> bloqueios
    ) {

        return bloqueios.stream().anyMatch(bloqueio ->

                inicioSlot.isBefore(bloqueio.getFim())
                        &&
                        fimSlot.isAfter(bloqueio.getInicio())

        );
    }

    private boolean temConflitoComAgendamento(
            LocalDateTime inicioSlot,
            LocalDateTime fimSlot,
            List<AgendamentoItem> agendamentos
    ) {

        return agendamentos.stream()
                .anyMatch(agendamento ->

                        inicioSlot.isBefore(
                                agendamento.getFimAtendimento()
                        )
                                &&
                                fimSlot.isAfter(
                                        agendamento.getInicioAtendimento()
                                )
                );
    }
}
