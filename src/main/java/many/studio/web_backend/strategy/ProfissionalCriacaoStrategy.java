package many.studio.web_backend.strategy;

import many.studio.web_backend.dto.UsuarioCriacaoDto;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.strategy.UsuarioCriacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalCriacaoStrategy implements UsuarioCriacaoStrategy {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalCriacaoStrategy(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    @Override
    public boolean suporta(Long perfilId) {
        return perfilId == 1L || perfilId == 2L;
    }

    @Override
    public void criarDadosComplementares(Usuario usuario, UsuarioCriacaoDto dto) {

        Profissional profissional = new Profissional();

        profissional.setNome(dto.getNome());
        profissional.setUsuario(usuario);

        profissionalRepository.save(profissional);
    }
}