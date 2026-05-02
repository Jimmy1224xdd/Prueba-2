package modelo;

import jakarta.persistence.*;
import java.util.Date;
import modelo.types.EstadoServicio;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private int idServicio;

    @Column(name = "titulo")
    private String tituloServicio;

    @Column(name = "descripcion")
    private String descripcionServicio;

    @Column(name = "precio")
    private double precioServicio;

    @Column(name = "disponibilidad")
    private boolean disponibilidad;

    @Column(name = "fecha_publicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPublicacionServicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoServicio estado;

    // RELACIÓN CON USUARIO (proveedor)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // RELACIÓN CON CATEGORIA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Constructor vacío (obligatorio JPA)
    public Servicio() {}

    // Constructor útil
    public Servicio(String titulo, String descripcion, double precio, Usuario usuario, Categoria categoria) {
        this.tituloServicio          = titulo;
        this.descripcionServicio     = descripcion;
        this.precioServicio          = precio;
        this.usuario                 = usuario;
        this.categoria               = categoria;
        this.disponibilidad          = true;
        this.fechaPublicacionServicio = new Date();
        this.estado                  = EstadoServicio.ACTIVO;
    }

    // ── MOVE METHOD ───────────────────────────────────────────────────────────
    // Movido desde DetalleServicioServlet y SolicitarServicioServlet:
    // la lógica que compara IDs de usuario pertenece al modelo, no al controlador.
    public boolean esPropietario(Usuario usuario) {
        return this.usuario.getIdUsuario() == usuario.getIdUsuario();
    }

    public void publicarServicio() {
        this.estado = EstadoServicio.ACTIVO;
        this.disponibilidad = true;
    }

    public void marcarDisponibilidad(EstadoServicio nuevoEstado) {
        this.estado = nuevoEstado;
        this.disponibilidad = nuevoEstado == EstadoServicio.ACTIVO;
    }

    // ── getters y setters ─────────────────────────────────────

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getTituloServicio() { return tituloServicio; }
    public void setTituloServicio(String tituloServicio) { this.tituloServicio = tituloServicio; }

    public String getDescripcionServicio() { return descripcionServicio; }
    public void setDescripcionServicio(String descripcionServicio) { this.descripcionServicio = descripcionServicio; }

    public double getPrecioServicio() { return precioServicio; }
    public void setPrecioServicio(double precioServicio) { this.precioServicio = precioServicio; }

    public boolean isDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(boolean disponibilidad) { this.disponibilidad = disponibilidad; }

    public Date getFechaPublicacionServicio() { return fechaPublicacionServicio; }
    public void setFechaPublicacionServicio(Date fechaPublicacionServicio) { this.fechaPublicacionServicio = fechaPublicacionServicio; }

    public EstadoServicio getEstado() { return estado; }
    public void setEstado(EstadoServicio estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
