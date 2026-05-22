package many.studio.web_backend.mapper;

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
}
