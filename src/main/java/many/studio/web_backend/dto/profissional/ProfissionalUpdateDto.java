package many.studio.web_backend.dto.profissional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public class ProfissionalUpdateDto{

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String documento;

    @NotBlank
    private String telefone;

    private String senha;

    private List<String> servicos;

    private List<DiasDeTrabalhoDto> diasDeTrabalho;

    private String fotoUrl;

    public static class DiasDeTrabalhoDto {
        private String diaSemana;
        private LocalTime inicio;
        private LocalTime fim;
        private Long servicoId;

        public DiasDeTrabalhoDto() {}

            public String getDiaSemana() {
                    return diaSemana;
            }

            public void setDiaSemana(String diaSemana) {
                    this.diaSemana = diaSemana;
            }

            public LocalTime getInicio() {
                    return inicio;
            }

            public void setInicio(LocalTime inicio) {
                    this.inicio = inicio;
            }

            public LocalTime getFim() {
                    return fim;
            }

            public void setFim(LocalTime fim) {
                    this.fim = fim;
            }

        public Long getServicoId() {
            return servicoId;
        }

        public void setServicoId(Long servicoId) {
            this.servicoId = servicoId;
        }
    }

    public ProfissionalUpdateDto() {}

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
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

        public String getTelefone() {
                return telefone;
        }

        public void setTelefone(String telefone) {
                this.telefone = telefone;
        }

        public String getSenha() {
                return senha;
        }

        public void setSenha(String senha) {
                this.senha = senha;
        }

        public List<String> getServicos() {
                return servicos;
        }

        public void setServicos(List<String> servicos) {
                this.servicos = servicos;
        }

        public List<DiasDeTrabalhoDto> getDiasDeTrabalho() {
                return diasDeTrabalho;
        }

        public void setDiasDeTrabalho(List<DiasDeTrabalhoDto> diasDeTrabalho) {
                this.diasDeTrabalho = diasDeTrabalho;
        }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}