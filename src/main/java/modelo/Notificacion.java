package modelo;

import jakarta.persistence.*;
import java.util.Date;
import modelo.types.TiposNotificacion;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;

    @Column(name = "contenido")
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion")
    private TiposNotificacion tipoNotificacion;

    @Column(name = "fecha_notificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNotificacion;

    @Column(name = "leida")
    private boolean leida;

    // 🔥 RELACIÓN CON USUARIO (destinatario)
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Constructor vacío
    public Notificacion() {}

    // Constructor útil
    public Notificacion(String contenido, TiposNotificacion tipoNotificacion, Usuario usuario) {
        this.contenido = contenido;
        this.tipoNotificacion = tipoNotificacion;
        this.usuario = usuario;
        this.fechaNotificacion = new Date();
        this.leida = false;
    }

    // ── métodos de negocio ─────────────────

    public void marcarNotificacionLeida() {
        this.leida = true;
    }

    // ── getters y setters ─────────────────

    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public TiposNotificacion getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(TiposNotificacion tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }

    public Date getFechaNotificacion() { return fechaNotificacion; }
    public void setFechaNotificacion(Date fechaNotificacion) { this.fechaNotificacion = fechaNotificacion; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}