package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria_servicos")
public class CategoriaServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoria;

    public CategoriaServico(Integer id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public CategoriaServico(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}