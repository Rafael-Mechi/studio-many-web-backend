package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.entity.Pacote;

import java.util.List;

public class PacoteMapper {


    public static PacoteListarDto  toResponse(Pacote pacote){
        PacoteListarDto response = new PacoteListarDto();
        response.setId(pacote.getId());
        response.setAtivo(pacote.getAtivo());
        response.setNome(pacote.getNome());
        response.setServico(pacote.getServico());
        response.setPrecoTotal(pacote.getPrecoTotal());
        response.setTotalSessoes(pacote.getTotalSessoes());
        response.setValidadeDias(pacote.getValidadeDias());

        return response;
    }


    public static List<PacoteListarDto> toResponse(List<Pacote> list){
        return list.stream().map(PacoteMapper :: toResponse).toList();
    }


    public static Pacote toEntity(PacoteCadastroDto dto){
        Pacote entity = new Pacote();
        entity.setNome(dto.getNome());
        entity.setTotalSessoes(dto.getTotalSessoes());
        entity.setCriadoEm(dto.getCriadoEm());
        entity.setAtivo(dto.getAtivo());
        entity.setValidadeDias(dto.getValidadeDias());
        entity.setPrecoTotal(dto.getPrecoTotal());
        entity.setServico(dto.getServico());
        return entity;
    }

    public static List<Pacote> toEntity(List<PacoteListarDto> dtos){
        return dtos.stream().map(PacoteMapper :: toEntity).toList();
    }
}
