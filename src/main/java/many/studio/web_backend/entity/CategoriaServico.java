package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria_servicos")
public class CategoriaServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;

    public CategoriaServico(Long id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public CategoriaServico(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}