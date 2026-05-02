package dao;

import modelo.Conversacion;
import modelo.Mensaje;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class MensajeDAO {

    public void guardar(Mensaje mensaje) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(mensaje);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar mensaje", e);
        }
    }

    public List<Mensaje> listarPorConversacion(Conversacion conversacion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Mensaje m WHERE m.conversacion = :conv ORDER BY m.fechaEnvio ASC";
            return session.createQuery(hql, Mensaje.class)
                    .setParameter("conv", conversacion)
                    .getResultList();
        }
    }
}
