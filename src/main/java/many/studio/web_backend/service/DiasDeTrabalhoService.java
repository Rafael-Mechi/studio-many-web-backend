package many.studio.web_backend.service;

import many.studio.web_backend.dto.calendario.DiasDeTrabalhoResponse;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.DiasDeTrabalhoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiasDeTrabalhoService {

    private final DiasDeTrabalhoRepository diasDeTrabalhoRepository;
    private final ProfissionalRepository profissionalRepository;

    public DiasDeTrabalhoService(DiasDeTrabalhoRepository diasDeTrabalhoRepository, ProfissionalRepository profissionalRepository) {
        this.diasDeTrabalhoRepository = diasDeTrabalhoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public DiasDeTrabalho criar(DiasDeTrabalho diasDeTrabalho) {

        Profissional profissional = profissionalRepository.findById(diasDeTrabalho.getProfissional().getId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        diasDeTrabalho.setProfissional(profissional);

        return diasDeTrabalhoRepository.save(diasDeTrabalho);
    }

    public List<DiasDeTrabalhoResponse> gerarDiasDisponiveisPorProfissional(Long profissionalId, YearMonth mes) {

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

        return diasDeTrabalhoNoMes;
    }




}
