package many.studio.web_backend.dto.usuario;

public class ProfissionalResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String especialidade;
    private Integer qtdAgendamentos;

    public ProfissionalResponse() {}

    public ProfissionalResponse(Long id, String nome, String telefone, String email, String especialidade, Integer qtdAgendamentos) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.especialidade = especialidade;
        this.qtdAgendamentos = qtdAgendamentos;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Integer getQtdAgendamentos() {
        return qtdAgendamentos;
    }

    public void setQtdAgendamentos(Integer qtdAgendamentos) {
        this.qtdAgendamentos = qtdAgendamentos;
    }
}
