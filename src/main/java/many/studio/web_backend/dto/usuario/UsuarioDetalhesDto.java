package many.studio.web_backend.dto.usuario;

import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetalhesDto implements UserDetails {

    private final Long id;
    private final String email;
    private final String senha;
    private final Perfil perfil;
    private final Boolean ativo;


    public UsuarioDetalhesDto(
            Long id, String email,
            String senha,
            Perfil perfil, Boolean ativo
    ) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public UsuarioDetalhesDto(Long id, Usuario usuario, Boolean ativo) {
        this.id = id;
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.perfil = usuario.getPerfil();
        this.ativo = ativo;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(perfil.getPerfil()));

    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(ativo);
    }
}
