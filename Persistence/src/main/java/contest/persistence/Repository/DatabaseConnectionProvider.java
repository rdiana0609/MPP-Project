package contest.persistence.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DatabaseConnectionProvider {
    private static Properties properties;
    private static final Logger logger = LogManager.getLogger();
    private static Connection instance = null;

    private DatabaseConnectionProvider() {
    }

    public static void setProperties(Properties properties) {
        DatabaseConnectionProvider.properties = properties;
    }
    private static Connection getNewConnection() {
        logger.traceEntry();

        String url = properties.getProperty("jdbc.url");
        logger.trace("Trying to connect to database ... {}", url);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            logger.info("Connected successfully to database");
        } catch (SQLException e) {
            logger.error("Connection failed with error: " + e);
        }

        logger.traceExit("Exiting with connection: {}", connection);
        return connection;
    }

    public static Connection getConnection() {
        logger.traceEntry();

        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException e) {
            logger.error("Error from database: " + e);
        }

        logger.traceExit("Exiting with connection: {}", instance);
        return instance;
    }
}
