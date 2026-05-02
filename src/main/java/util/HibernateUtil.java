package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

/**
 * HibernateUtil configura la conexión a la BD leyendo primero las variables
 * de entorno (para producción en Railway/Oracle/etc.) y si no existen,
 * cae de vuelta a los valores del hibernate.cfg.xml (para desarrollo local).
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();

            // Leer variables de entorno de Railway (o cualquier plataforma cloud)
            String host     = System.getenv("MYSQL_HOST");
            String port     = System.getenv("MYSQL_PORT");
            String database = System.getenv("MYSQL_DATABASE");
            String user     = System.getenv("MYSQL_USER");
            String password = System.getenv("MYSQL_PASSWORD");

            // Si existen las variables de entorno, sobreescribir la config del XML
            if (host != null && database != null && user != null && password != null) {
                String dbPort = (port != null) ? port : "3306";
                String jdbcUrl = "jdbc:mysql://" + host + ":" + dbPort + "/" + database
                        + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true";

                Properties props = new Properties();
                props.setProperty("hibernate.connection.url",      jdbcUrl);
                props.setProperty("hibernate.connection.username", user);
                props.setProperty("hibernate.connection.password", password);
                props.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

                configuration.addProperties(props);

                System.out.println("[HibernateUtil] Usando configuración desde variables de entorno (producción).");
                System.out.println("[HibernateUtil] Conectando a: " + host + ":" + dbPort + "/" + database);
            } else {
                System.out.println("[HibernateUtil] Variables de entorno no encontradas. Usando hibernate.cfg.xml (desarrollo local).");
            }

            sessionFactory = configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Error al crear SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}