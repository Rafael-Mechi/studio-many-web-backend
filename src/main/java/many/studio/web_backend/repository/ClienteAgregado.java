package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Cliente;

import java.time.LocalDate;

public interface ClienteAgregado {
    Cliente getCliente();
    LocalDate getUltimaVisita();
    Double getTotalGasto();
}
