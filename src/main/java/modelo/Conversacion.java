package modelo;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversaciones")
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversacion")
    private int idConversacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Usuario proveedor;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @OneToMany(mappedBy = "conversacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mensaje> mensajes;

    public Conversacion() {}

    public Conversacion(Usuario cliente, Usuario proveedor, Servicio servicio) {
        this.cliente = cliente;
        this.proveedor = proveedor;
        this.servicio = servicio;
        this.fechaCreacion = new Date();
    }

    public int getIdConversacion() { return idConversacion; }
    public void setIdConversacion(int idConversacion) { this.idConversacion = idConversacion; }

    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public Usuario getProveedor() { return proveedor; }
    public void setProveedor(Usuario proveedor) { this.proveedor = proveedor; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<Mensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }
}
