package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuario_Id(Long usuarioId);

    @Query("""
        SELECT c as cliente, 
               MAX(a.criadoEm) as ultimaVisita, 
               SUM(CASE WHEN sa.estado = 'Pago' THEN a.precoFinal ELSE 0.0 END) as totalGasto 
        FROM Cliente c 
        JOIN Agendamento a ON a.cliente.id = c.id 
        JOIN a.statusAgendamento sa 
        WHERE a.profissional.id = :profissionalId 
        GROUP BY c.id, c.nome
    """)
    List<ClienteAgregado> findClientesByProfissionalId(@Param("profissionalId") Long profesionalId);

    @Query("""
        SELECT c as cliente, 
               MAX(a.criadoEm) as ultimaVisita, 
               SUM(CASE WHEN sa.estado = 'Pago' THEN a.precoFinal ELSE 0.0 END) as totalGasto 
        FROM Cliente c 
        JOIN Agendamento a ON a.cliente.id = c.id 
        JOIN a.statusAgendamento sa 
        WHERE a.profissional.id = :profissionalId AND c.id = :clienteId 
        GROUP BY c.id, c.nome
    """)
    Optional<ClienteAgregado> findClienteByProfissionalIdEClienteId(
            @Param("profissionalId") Long profissionalId,
            @Param("clienteId") Long clienteId
    );

        @Query("""
        SELECT DISTINCT c
        FROM Cliente c
        JOIN Agendamento a ON a.cliente.id = c.id
        WHERE a.profissional.usuario.id = :usuarioId
    """)
    List<Cliente> findClientesByProfissionalUsuarioId(
            @Param("usuarioId") Long usuarioId
    );
}
