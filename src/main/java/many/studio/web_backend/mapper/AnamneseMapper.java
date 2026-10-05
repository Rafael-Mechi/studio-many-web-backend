package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.anamnese.AnamneseListResponse;
import many.studio.web_backend.dto.anamnese.AnamneseRequest;
import many.studio.web_backend.dto.anamnese.AnamneseResponse;
import many.studio.web_backend.entity.Anamnese;

public class AnamneseMapper {
    public static AnamneseResponse toResponse(Anamnese anamnese){
        AnamneseResponse anamneseResponse = new AnamneseResponse();

        anamneseResponse.setId(anamnese.getId());
        anamneseResponse.setNomeCliente(anamnese.getCliente().getNome());
        anamneseResponse.setInformacao(anamnese.getInformacao());

        return anamneseResponse;
    }

    public static AnamneseListResponse toListResponse(Anamnese anamnese){
        AnamneseListResponse anamneseListResponse = new AnamneseListResponse();

        anamneseListResponse.setId(anamnese.getId());
        anamneseListResponse.setNomeCliente(anamnese.getCliente().getNome());
        anamneseListResponse.setTelefoneCliente(anamnese.getCliente().getTelefone());
        anamneseListResponse.setInformacao(anamnese.getInformacao());

        return anamneseListResponse;
    }

}
