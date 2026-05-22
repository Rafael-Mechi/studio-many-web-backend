package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Servico;

import java.util.List;


public class ServicoMapper {

    public static ServicoListarDto toResponse(Servico servico){
        ServicoListarDto response = new ServicoListarDto();
        response.setId(servico.getId());
        response.setCriadoEm(servico.getCriadoEm());
        response.setAtivo(servico.getAtivo());
        response.setNome(servico.getNome());
        response.setDescricao(servico.getDescricao());
        response.setDuracaoMinutos(servico.getDuracaoMinutos());
        response.setPreco(servico.getPreco());
        response.setFotoUrl(servico.getFotoUrl());
        response.setSinalValor(servico.getSinalValor());

        return response;
    }

    public static List<ServicoListarDto> toResponse(List<Servico>list){
        return list.stream().map(ServicoMapper::toResponse).toList();
    }
}
