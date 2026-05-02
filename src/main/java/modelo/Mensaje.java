package modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private int idMensaje;

    @ManyToOne
    @JoinColumn(name = "id_conversacion")
    private Conversacion conversacion;

    @ManyToOne
    @JoinColumn(name = "id_remitente")
    private Usuario remitente;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;

    public Mensaje() {}

    public Mensaje(Conversacion conversacion, Usuario remitente, String contenido) {
        this.conversacion = conversacion;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaEnvio = new Date();
    }

    public int getIdMensaje() { return idMensaje; }
    public void setIdMensaje(int idMensaje) { this.idMensaje = idMensaje; }

    public Conversacion getConversacion() { return conversacion; }
    public void setConversacion(Conversacion conversacion) { this.conversacion = conversacion; }

    public Usuario getRemitente() { return remitente; }
    public void setRemitente(Usuario remitente) { this.remitente = remitente; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
