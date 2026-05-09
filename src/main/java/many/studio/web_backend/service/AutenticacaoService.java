package many.studio.web_backend.service;


import many.studio.web_backend.dto.UsuarioDetalhesDto;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuário: %s não encontrado", username));
        }

        Usuario usuario = usuarioOpt.get();

        // Passa o campo 'ativo' do banco para que isEnabled() bloqueie usuários inativos
        return new UsuarioDetalhesDto(usuario, usuario.getAtivo());
    }
}
