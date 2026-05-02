package dao;

import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.Optional;

public class UsuarioDAO {

    // 🔹 GUARDAR (registro)
    public void guardar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(usuario);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e);
        }
    }

    // 🔹 BUSCAR POR ID
    public Optional<Usuario> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Usuario usuario = session.get(Usuario.class, id);
            return Optional.ofNullable(usuario);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 🔥 BUSCAR POR CORREO (clave para login)
    public Optional<Usuario> buscarPorCorreo(String correo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Usuario u WHERE u.correo = :correo";

            Usuario usuario = session.createQuery(hql, Usuario.class)
                    .setParameter("correo", correo)
                    .uniqueResult();

            return Optional.ofNullable(usuario);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 🔥 LOGIN
    public Optional<Usuario> login(String correo, String contrasena) {
        Optional<Usuario> usuario = buscarPorCorreo(correo);

        if (usuario.isPresent()) {
            Usuario u = usuario.get();

            // ⚠️ aquí puedes luego usar hash
            if (u.getContrasena().equals(contrasena) && u.isActivo()) {
                return usuario;
            }
        }

        return Optional.empty();
    }

    // 🔹 ACTUALIZAR
    public void actualizar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(usuario);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e);
        }
    }

    // 🔹 ELIMINAR
    public void eliminar(Usuario usuario) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.remove(usuario);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e);
        }
    }

    // 🔹 VALIDAR SI EXISTE CORREO
    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo).isPresent();
    }
}