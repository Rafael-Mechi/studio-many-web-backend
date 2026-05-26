package many.studio.web_backend.dto.calendario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class DiasDeTrabalhoResponse {

    private LocalDate data;
    private LocalTime inicio;
    private LocalTime fim;
    private ProfissionalDto profissional;

    public DiasDeTrabalhoResponse(LocalDate data, LocalTime inicio, LocalTime fim, ProfissionalDto profissional) {
        this.data = data;
        this.inicio = inicio;
        this.fim = fim;
        this.profissional = profissional;
    }

    public static class ProfissionalDto {
        private Long id;
        private String nome;

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
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public ProfissionalDto getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalDto profissional) {
        this.profissional = profissional;
    }
}
