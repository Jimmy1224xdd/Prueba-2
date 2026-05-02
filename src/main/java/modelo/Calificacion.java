package modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "calificaciones")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private int idCalificacion;

    @Column(name = "puntuacion")
    private int puntuacion;

    @Column(name = "resena")
    private String resena;

    @Column(name = "fecha_calificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCalificacion;

    // 🔥 RELACIÓN CON SERVICIO
    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    // 🔥 RELACIÓN CON USUARIO (quien califica)
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Constructor vacío
    public Calificacion() {}

    // Constructor útil
    public Calificacion(int puntuacion, String resena, Servicio servicio, Usuario usuario) {
        this.puntuacion = puntuacion;
        this.resena = resena;
        this.servicio = servicio;
        this.usuario = usuario;
        this.fechaCalificacion = new Date();
    }

    // ── getters y setters ─────────────────

    public int getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(int idCalificacion) { this.idCalificacion = idCalificacion; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getResena() { return resena; }
    public void setResena(String resena) { this.resena = resena; }

    public Date getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(Date fechaCalificacion) { this.fechaCalificacion = fechaCalificacion; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}