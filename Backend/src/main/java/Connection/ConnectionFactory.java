package Connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private final String schema;
    private final int port;
    private static Connection connection = null;
    static Dotenv dotenv = Dotenv.configure().load();
    static final String password = dotenv.get("AWS_RDS_PASSWORD");
    static final String username = dotenv.get("AWS_RDS_USERNAME");

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            connection = new ConnectionFactory().getAwsConnection();
        }
        return connection;
    }

    private ConnectionFactory() {
        this.schema = "public";
        this.port = 5432;
    }


    public Connection getAwsConnection() throws SQLException {
        if (connection == null) {
            String url = "jdbc:postgresql://kumarnetaws.cnhcl6efo88h.us-east-1.rds.amazonaws.com:" + port + "/kumarnet?user=" + username + "&password=" + password;
            connection = DriverManager.getConnection(url);
            connection.setSchema("public");
            System.out.println("Connected to database, schema: " + schema);
        }
        return connection;
    }
}
