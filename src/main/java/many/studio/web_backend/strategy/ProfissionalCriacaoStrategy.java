package many.studio.web_backend.strategy;

import many.studio.web_backend.dto.usuario.UsuarioCriacaoDto;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalCriacaoStrategy implements UsuarioCriacaoStrategy {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalCriacaoStrategy(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    @Override
    public boolean suporta(Integer perfilId) {
        return perfilId == 1 || perfilId == 2;
    }

    @Override
    public void criarDadosComplementares(Usuario usuario, UsuarioCriacaoDto dto) {

        Profissional profissional = new Profissional();

        profissional.setNome(dto.getNome());
        profissional.setUsuario(usuario);

        profissionalRepository.save(profissional);
    }
}