package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Servico;

import java.util.List;


public class ServicoMapper {

    public static Servico toEntity(ServicoCadastroDto cadastroDto){
        Servico entity = new Servico();
        entity.setDescricao(cadastroDto.getDescricao());
        entity.setAtivo(cadastroDto.getAtivo());
        entity.setNome(cadastroDto.getNome());
        entity.setPreco(cadastroDto.getPreco());
        entity.setFotoUrl(cadastroDto.getFotoUrl());
        entity.setDuracaoMinutos(cadastroDto.getDuracaoMinutos());
        entity.setCriadoEm(cadastroDto.getCriadoEm());
        entity.setSinalValor(cadastroDto.getSinalValor());
        return entity;
    }

    public static List<Servico> toEntity(List<ServicoCadastroDto> cadastroDto){
        return cadastroDto.stream().map(ServicoMapper::toEntity).toList();
    }


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
