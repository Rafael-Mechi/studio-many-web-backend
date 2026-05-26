package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico,Long> {
    Optional<Servico> findByNome(String nome);

    List<Servico> findAllByProfissionalId(Long id);


}
