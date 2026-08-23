package many.studio.web_backend.service;

import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeRequest;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.DiaDisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.FuncionarioDisponibilidadeResponse;
import many.studio.web_backend.dto.selecao_agendamento.auxiliar.MesDisponibilidadeResponse;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.DiasDeTrabalhoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadeService {
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final DiasDeTrabalhoRepository diaDeTrabalhoRepository;

    public DisponibilidadeService(ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository, DiasDeTrabalhoRepository diaDeTrabalhoRepository) {
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.diaDeTrabalhoRepository = diaDeTrabalhoRepository;
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

            List<MesDisponibilidadeResponse> meses =
                    new ArrayList<>();

            LocalDate dataAtual = hoje;

            while (!dataAtual.isAfter(limite)) {

                LocalDate finalDataAtual = dataAtual;
                boolean trabalhaNesseDia =
                        diasDeTrabalho.stream()
                                .anyMatch(dia ->
                                        dia.getDiaDaSemana()
                                                .equals(
                                                        finalDataAtual.getDayOfWeek()
                                                )
                                );

                if (trabalhaNesseDia) {

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

                                        novoMes.setAno(finalDataAtual.getYear());
                                        novoMes.setMes(
                                                finalDataAtual.getMonthValue()
                                        );

                                        novoMes.setDias(
                                                new ArrayList<>()
                                        );

                                        meses.add(novoMes);

                                        return novoMes;
                                    });

                    DiaDisponibilidadeResponse diaResponse =
                            new DiaDisponibilidadeResponse();

                    diaResponse.setDia(
                            dataAtual.getDayOfMonth()
                    );

                    diaResponse.setHorarios(
                            new ArrayList<>()
                    );

                    mesResponse.getDias()
                            .add(diaResponse);
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
}
