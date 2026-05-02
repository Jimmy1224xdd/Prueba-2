package modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private int idSolicitud;

    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;

    // 🔥 RELACIÓN CON USUARIO
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // 🔥 RELACIÓN CON SERVICIO
    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private modelo.types.EstadoSolicitud estado;

    // Constructor vacío
    public Solicitud() {}

    // Constructor útil
    public Solicitud(Usuario usuario, Servicio servicio) {
        this.usuario = usuario;
        this.servicio = servicio;
        this.fechaSolicitud = new Date();
        this.estado = modelo.types.EstadoSolicitud.SOLICITADO;
    }

    // getters y setters

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public modelo.types.EstadoSolicitud getEstado() { return estado; }
    public void setEstado(modelo.types.EstadoSolicitud estado) { this.estado = estado; }
}