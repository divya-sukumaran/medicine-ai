package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for creating JDBC connections to the
 * PostgreSQL database used by the AI Health Assistant application
 * (Supabase in production, a local Postgres container in development).
 * <p>
 * Connection details are read from the {@code DB_HOST}, {@code DB_PORT},
 * {@code DB_NAME}, {@code DB_USER}, {@code DB_PASSWORD}, and
 * {@code DB_SSLMODE} environment variables. If unset, they default to a
 * typical local Postgres install. For Supabase, set {@code DB_SSLMODE}
 * to {@code require} and use the connection-pooler host from the
 * Supabase dashboard.
 */
public class DBConnection {

    private static final String HOST = env("DB_HOST", "localhost");
    private static final String PORT = env("DB_PORT", "5432");
    private static final String DATABASE = env("DB_NAME", "ai_health_assistant");
    private static final String USERNAME = env("DB_USER", "postgres");
    private static final String PASSWORD = env("DB_PASSWORD", "postgres");
    private static final String SSL_MODE = env("DB_SSLMODE", "disable");

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE + "?sslmode=" + SSL_MODE;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    private DBConnection() {
        // Prevent instantiation of utility class
    }

    /**
     * Opens and returns a new connection to the PostgreSQL database.
     *
     * @return an active {@link Connection} object
     * @throws SQLException if the connection could not be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
