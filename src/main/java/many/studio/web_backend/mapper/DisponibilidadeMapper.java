package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.selecao_agendamento.auxiliar.FuncionarioDisponibilidadeResponse;
import many.studio.web_backend.entity.Profissional;

import java.util.ArrayList;

public class DisponibilidadeMapper {

    public static FuncionarioDisponibilidadeResponse  toFuncionarioResponse(Profissional profissional) {

        FuncionarioDisponibilidadeResponse response = new FuncionarioDisponibilidadeResponse();

        response.setFuncionarioId(profissional.getId());

        response.setNome(profissional.getNome());

        response.setMeses(new ArrayList<>());

        return response;
    }
}