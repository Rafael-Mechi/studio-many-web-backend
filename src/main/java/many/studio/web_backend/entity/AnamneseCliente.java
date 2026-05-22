package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "anamnese_clientes")
public class AnamneseCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "anamneses_id")
    private Anamnese anamnese;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clientes_id")
    private Cliente cliente;

    public AnamneseCliente() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Anamnese getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(Anamnese anamnese) {
        this.anamnese = anamnese;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}