package many.studio.web_backend.dto.usuario;

public class UsuarioPerfilResponseDto {
    private Long id;
    private Long clienteId;
    private String nome;
    private String clienteTelefone;
    private String role;

    public UsuarioPerfilResponseDto(Long id, Long clienteId, String nome, String clienteTelefone, String role) {
        this.id = id;
        this.clienteId = clienteId;
        this.nome = nome;
        this.clienteTelefone = clienteTelefone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }
}
