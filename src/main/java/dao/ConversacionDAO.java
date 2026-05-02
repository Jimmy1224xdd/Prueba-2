package dao;

import modelo.Conversacion;
import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class ConversacionDAO {

    public void guardar(Conversacion conversacion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(conversacion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar conversacion", e);
        }
    }

    public List<Conversacion> listarPorUsuario(Usuario usuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Conversacion c WHERE c.cliente = :u OR c.proveedor = :u ORDER BY c.fechaCreacion DESC";
            return session.createQuery(hql, Conversacion.class)
                    .setParameter("u", usuario)
                    .getResultList();
        }
    }

    public Optional<Conversacion> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Conversacion.class, id));
        }
    }

    public Optional<Conversacion> buscarPorUsuariosYServicio(Usuario cliente, Usuario proveedor, int idServicio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Conversacion c WHERE c.cliente = :cliente AND c.proveedor = :proveedor AND c.servicio.idServicio = :idServicio";
            List<Conversacion> list = session.createQuery(hql, Conversacion.class)
                    .setParameter("cliente", cliente)
                    .setParameter("proveedor", proveedor)
                    .setParameter("idServicio", idServicio)
                    .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        }
    }
}
