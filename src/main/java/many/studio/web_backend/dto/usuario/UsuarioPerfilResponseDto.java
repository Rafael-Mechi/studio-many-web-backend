package many.studio.web_backend.dto.usuario;

public class UsuarioPerfilResponseDto {
    private Long id;
    private String nome;
    private String role;

    public UsuarioPerfilResponseDto(Long id, String nome, String role) {
        this.id = id;
        this.nome = nome;
        this.role = role;
    }

    public UsuarioPerfilResponseDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
