package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Cliente;

import java.time.LocalDateTime;

public interface ClienteAgregado {
    Cliente getCliente();
    LocalDateTime getUltimaVisita();
    Double getTotalGasto();
}
