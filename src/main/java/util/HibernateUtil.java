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

            // Leer variables de entorno (preferencia a JDBC_URL completa)
            String jdbcUrlEnv = System.getenv("JDBC_URL");
            String host       = System.getenv("MYSQL_HOST");
            String port       = System.getenv("MYSQL_PORT");
            String database   = System.getenv("MYSQL_DATABASE");
            String user       = System.getenv("MYSQL_USER");
            String password   = System.getenv("MYSQL_PASSWORD");

            Properties props = new Properties();

            if (jdbcUrlEnv != null && !jdbcUrlEnv.isEmpty()) {
                // Si existe JDBC_URL completa, usarla directamente
                props.setProperty("hibernate.connection.url", jdbcUrlEnv);
                if (user != null) props.setProperty("hibernate.connection.username", user);
                if (password != null) props.setProperty("hibernate.connection.password", password);
                
                configuration.addProperties(props);
                System.out.println("[HibernateUtil] Usando JDBC_URL desde variables de entorno.");
            } else if (host != null && database != null && user != null && password != null) {
                // Si no hay JDBC_URL pero hay variables individuales, construirla
                String dbPort = (port != null) ? port : "3306";
                String jdbcUrl = "jdbc:mysql://" + host + ":" + dbPort + "/" + database
                        + "?useSSL=true&verifyServerCertificate=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true&connectTimeout=5000&socketTimeout=5000";

                props.setProperty("hibernate.connection.url",      jdbcUrl);
                props.setProperty("hibernate.connection.username", user);
                props.setProperty("hibernate.connection.password", password);
                props.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

                configuration.addProperties(props);

                System.out.println("[HibernateUtil] Usando configuración desde variables de entorno (individuales).");
                System.out.println("[HibernateUtil] Conectando a: " + host + ":" + dbPort + "/" + database);
            } else {
                System.out.println("[HibernateUtil] Variables de entorno no encontradas o incompletas. Usando hibernate.cfg.xml.");
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