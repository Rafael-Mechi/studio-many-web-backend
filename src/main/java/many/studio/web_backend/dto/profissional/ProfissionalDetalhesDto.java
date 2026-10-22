package many.studio.web_backend.dto.profissional;

import java.time.LocalDate;
import java.util.List;

public class ProfissionalDetalhesDto {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String documento;
    private String fotoUrl;
    private String especialidade;
    private List<String> servicosRealizados;
    private List<DiasDeTrabalhoDto> diasDeTrabalho;

    public ProfissionalDetalhesDto() {}


    private static class DiasDeTrabalhoDto{
        private String diaSemana;
        private LocalDate inicio;
        private LocalDate fim;

        public DiasDeTrabalhoDto() {}

        public String getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(String diaSemana) {
            this.diaSemana = diaSemana;
        }

        public LocalDate getInicio() {
            return inicio;
        }

        public void setInicio(LocalDate inicio) {
            this.inicio = inicio;
        }

        public LocalDate getFim() {
            return fim;
        }

        public void setFim(LocalDate fim) {
            this.fim = fim;
        }
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<String> getServicosRealizados() {
        return servicosRealizados;
    }

    public void setServicosRealizados(List<String> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }

    public List<DiasDeTrabalhoDto> getDiasDeTrabalho() {
        return diasDeTrabalho;
    }

    public void setDiasDeTrabalho(List<DiasDeTrabalhoDto> diasDeTrabalho) {
        this.diasDeTrabalho = diasDeTrabalho;
    }
}
