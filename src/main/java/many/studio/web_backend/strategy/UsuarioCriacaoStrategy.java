package many.studio.web_backend.strategy;

import many.studio.web_backend.dto.UsuarioCriacaoDto;
import many.studio.web_backend.entity.Usuario;

public interface UsuarioCriacaoStrategy {
    boolean suporta(Integer perfilId);

    void criarDadosComplementares(Usuario usuario, UsuarioCriacaoDto dto);
}
