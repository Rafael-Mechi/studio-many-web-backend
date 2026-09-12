package many.studio.web_backend.mapper.comprovante;

import many.studio.web_backend.dto.comprovante.ComprovanteResponse;
import many.studio.web_backend.entity.ComprovantePrv;

public class ComprovanteMapper {
    public static ComprovanteResponse toResponse(ComprovantePrv comprovantePrv){
        ComprovanteResponse cr = new ComprovanteResponse();
        cr.setPdf(comprovantePrv.getPdf());
        return cr;
    }
}
