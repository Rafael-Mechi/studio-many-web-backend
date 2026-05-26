package many.studio.web_backend.mapper.calendario;

import many.studio.web_backend.dto.calendario.DiasDeTrabalhoCriacaoResponse;
import many.studio.web_backend.dto.calendario.DiasDeTrabalhoRequest;
import many.studio.web_backend.dto.calendario.DiasDeTrabalhoResponse;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;

public class DiasDeTrabalhoMapper {

    public static DiasDeTrabalho toEntity(DiasDeTrabalhoRequest request, Long profissionalId) {

        Profissional profissional = new Profissional();
        profissional.setId(profissionalId);

        DiasDeTrabalho diasDeTrabalho = new DiasDeTrabalho();
        diasDeTrabalho.setDiaDaSemana(request.getDiaDaSemana());
        diasDeTrabalho.setHoraInicio(request.getInicio());
        diasDeTrabalho.setHoraFim(request.getFim());
        diasDeTrabalho.setProfissional(profissional);

        return diasDeTrabalho;
    }

    public static DiasDeTrabalhoCriacaoResponse toResponse(DiasDeTrabalho diasDeTrabalho) {

        DiasDeTrabalhoCriacaoResponse response = new DiasDeTrabalhoCriacaoResponse();
        response.setId(diasDeTrabalho.getId());
        response.setDiaDaSemana(diasDeTrabalho.getDiaDaSemana());
        response.setInicio(diasDeTrabalho.getHoraInicio());
        response.setFim(diasDeTrabalho.getHoraFim());
        response.setProfissionalId(diasDeTrabalho.getProfissional().getId());

        return response;
    }
}
