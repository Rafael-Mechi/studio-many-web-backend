package many.studio.web_backend.dto.servico;

public class ProfissionaisPorServicoDto {
    private Long id;
    private String nomeFuncionario;

    public ProfissionaisPorServicoDto(Long id, String nomeFuncionario) {
        this.id = id;
        this.nomeFuncionario = nomeFuncionario;
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
}
