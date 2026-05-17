package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    Optional<Servico> findByNome(String nome);
}
