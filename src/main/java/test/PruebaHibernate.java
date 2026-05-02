package test;

import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class PruebaHibernate {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Usuario u = new Usuario();
        u.setNombre("Juan");

        session.save(u);

        tx.commit();
        session.close();

        System.out.println("Usuario guardado correctamente ✅");
    }
}