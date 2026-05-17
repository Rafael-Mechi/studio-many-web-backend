package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perfis")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfil", nullable = false)
    private String perfil;

    public Perfil() {}

    public Perfil(Long id, String perfil) {
        this.id = id;
        this.perfil = perfil;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}