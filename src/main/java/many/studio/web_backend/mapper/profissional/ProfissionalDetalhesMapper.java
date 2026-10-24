package many.studio.web_backend.mapper.profissional;

import many.studio.web_backend.dto.profissional.ProfissionalDetalhesDto;
import many.studio.web_backend.entity.Profissional;

import java.util.List;

public class ProfissionalDetalhesMapper {

    public static ProfissionalDetalhesDto toResponse(Profissional profissional, List<String> servicosRealizados) {

        ProfissionalDetalhesDto response = new ProfissionalDetalhesDto();
        List<ProfissionalDetalhesDto.DiasDeTrabalhoDto> diasDeTrabalhoDto = DiasDeTrabalhoMapper.toResponseList(profissional.getDiasDeTrabalho());

        response.setNome(profissional.getNome());
        response.setTelefone(profissional.getTelefone());
        response.setEmail(profissional.getUsuario().getEmail());
        response.setDocumento(profissional.getDocumento());
        response.setFotoUrl(profissional.getFotoUrl());
        response.setDiasDeTrabalho(diasDeTrabalhoDto);
        response.setServicosRealizados(servicosRealizados);

        return response;
    }
}
