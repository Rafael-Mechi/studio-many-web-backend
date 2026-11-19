package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuario_Id(Long usuarioId);

    Optional<Cliente> findFirstByTelefone(String telefone);

    @Query("""
    SELECT c.usuario
    FROM Cliente c
    WHERE c.id = :clienteId
""")
    Optional<Usuario> findUsuarioByClienteId(@Param("clienteId") Long clienteId);

    @Query("""
    SELECT DISTINCT a.cliente
    FROM Agendamento a
    JOIN a.itens ai
    WHERE ai.profissional.usuario.id = :usuarioId
""")
    List<Cliente> findClientesByProfissionalUsuarioId(
            @Param("usuarioId") Long usuarioId
    );

    @Query("""
        SELECT c
        FROM Cliente c
        WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
           OR c.telefone LIKE CONCAT('%', :busca, '%')
        """)
    List<Cliente> buscarPorNomeOuTelefone(
            @Param("busca") String busca
    );
}
