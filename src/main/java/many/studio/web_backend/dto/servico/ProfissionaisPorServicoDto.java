package many.studio.web_backend.dto.servico;

public class ProfissionaisPorServicoDto {
    private Long id;
    private String nomeFuncionario;
    private String fotoUrl;

    public ProfissionaisPorServicoDto(Long id, String nomeFuncionario) {
        this.id = id;
        this.nomeFuncionario = nomeFuncionario;
    }

    public ProfissionaisPorServicoDto(Long id, String nomeFuncionario, String fotoUrl) {
        this.id = id;
        this.nomeFuncionario = nomeFuncionario;
        this.fotoUrl = fotoUrl;
    }

    public ProfissionaisPorServicoDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}