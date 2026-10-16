package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Servico;

import java.util.List;


public class ServicoMapper {

    public static Servico toEntity(ServicoCadastroDto cadastroDto){
        Servico entity = new Servico();
        atualizarEntity(entity, cadastroDto);
        return entity;
    }

    /** Copia os campos editáveis do DTO para uma entidade já existente (categoria e criadoEm ficam com o service). */
    public static void atualizarEntity(Servico entity, ServicoCadastroDto cadastroDto){
        entity.setDescricao(cadastroDto.getDescricao());
        entity.setAtivo(cadastroDto.getAtivo());
        entity.setNome(cadastroDto.getNome());
        entity.setPreco(cadastroDto.getPreco());
        entity.setFotoUrl(cadastroDto.getFotoUrl());
        entity.setDuracaoMinutos(cadastroDto.getDuracaoMinutos());
        entity.setSinalValor(cadastroDto.getSinalValor());
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

        if (servico.getCategoriaServico() != null) {
            response.setCategoriaId(servico.getCategoriaServico().getId());
            response.setCategoria(servico.getCategoriaServico().getCategoria());
        }

        return response;
    }

    public static List<ServicoListarDto> toResponse(List<Servico>list){
        return list.stream().map(ServicoMapper::toResponse).toList();
    }
}
