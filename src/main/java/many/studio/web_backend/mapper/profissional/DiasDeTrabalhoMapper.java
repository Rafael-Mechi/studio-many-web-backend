package many.studio.web_backend.mapper.profissional;

import many.studio.web_backend.dto.profissional.ProfissionalDetalhesDto;
import many.studio.web_backend.entity.DiasDeTrabalho;

import java.util.List;

public class DiasDeTrabalhoMapper {

    public static ProfissionalDetalhesDto.DiasDeTrabalhoDto toResponse(DiasDeTrabalho diasDeTrabalho) {
        ProfissionalDetalhesDto.DiasDeTrabalhoDto response = new ProfissionalDetalhesDto.DiasDeTrabalhoDto();

        response.setDiaSemana(diasDeTrabalho.getDiaDaSemana().toString());
        response.setInicio(diasDeTrabalho.getHoraInicio());
        response.setFim(diasDeTrabalho.getHoraFim());

        return response;
    }

    public static List<ProfissionalDetalhesDto.DiasDeTrabalhoDto> toResponseList(List<DiasDeTrabalho> diasDeTrabalho) {
        return diasDeTrabalho.stream()
                .map(DiasDeTrabalhoMapper::toResponse)
                .toList();
    }
}
