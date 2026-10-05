package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Anamnese;
import many.studio.web_backend.entity.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamneseRepository extends JpaRepository<Anamnese, Long> {

    Page<Anamnese> findByProfissional(
            Profissional profissional,
            Pageable pageable
    );

    @Query("""
        SELECT a
        FROM Anamnese a
        JOIN a.cliente c
        WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
           OR c.telefone LIKE CONCAT('%', :busca, '%')
        """)
    Page<Anamnese> buscarPorNomeOuTelefone(
            @Param("busca") String busca,
            Pageable pageable
    );

    @Query("""
        SELECT a
        FROM Anamnese a
        JOIN a.cliente c
        WHERE a.profissional = :profissional
          AND (
                LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
                OR c.telefone LIKE CONCAT('%', :busca, '%')
              )
        """)
    Page<Anamnese> buscarPorProfissionalNomeOuTelefone(
            @Param("profissional") Profissional profissional,
            @Param("busca") String busca,
            Pageable pageable
    );
}