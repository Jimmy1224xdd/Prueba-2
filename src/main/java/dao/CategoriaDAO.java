package dao;

import modelo.Categoria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class CategoriaDAO {

    // LISTAR TODAS
    public List<Categoria> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Categoria c ORDER BY c.idCategoria";

            return session.createQuery(hql, Categoria.class)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar categorias", e);
        }
    }

    // BUSCAR POR ID
    public Optional<Categoria> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Categoria categoria = session.get(Categoria.class, id);
            return Optional.ofNullable(categoria);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar categoria", e);
        }
    }

    // GUARDAR
    public void guardar(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(categoria);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar categoria", e);
        }
    }

    // ACTUALIZAR
    public void actualizar(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(categoria);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar categoria", e);
        }
    }

    // ELIMINAR
    public void eliminar(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.remove(categoria);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar categoria", e);
        }
    }
}