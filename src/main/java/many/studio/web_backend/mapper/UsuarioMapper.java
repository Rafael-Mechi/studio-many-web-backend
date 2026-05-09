package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.*;
import many.studio.web_backend.entity.Usuario;

public class UsuarioMapper {

    public static Usuario of(UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioCriacaoDto.getEmail());
        usuario.setSenha(usuarioCriacaoDto.getSenha());

        return usuario;
    }

    public static Usuario of(UsuarioLoginDto usuarioLoginDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioLoginDto.getEmail());
        usuario.setSenha(usuarioLoginDto.getSenha());

        return usuario;
    }

    // Retorna id + email + token. Nome não existe em 'usuarios'.
    public static UsuarioTokenDto of(Usuario usuario, String token) {
        UsuarioTokenDto dto = new UsuarioTokenDto();

        dto.setUserId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setToken(token);

        return dto;
    }

    public static UsuarioSessaoDto ofSessao(UsuarioTokenDto tokenDto) {
        UsuarioSessaoDto dto = new UsuarioSessaoDto();

        dto.setUserId(tokenDto.getUserId());
        dto.setEmail(tokenDto.getEmail());

        return dto;
    }

    public static UsuarioListarDto of(Usuario usuario) {
        UsuarioListarDto dto = new UsuarioListarDto();

        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());

        return dto;
    }
}
