package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnection {

    private transient Connection conn;

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // url for local database
    //private static final String DB_URL = "jdbc:mysql://localhost:3306/laserauth";
    // url for database on the server
    static final String DB_URL = "jdbc:mysql://projects-db.ewi.tudelft.nl:3306/"
            + "projects_LaserReflectionGroup62";

    // user info for local db
    //    private static final String USER_NAME = "root";
    //    private static final String PASSWORD = "SEMmysql";
    // user info for server db
    static final String USER_NAME = "pu_sIr7Qpz95QMFw";
    static final String PASSWORD = "9hrNkFAFnayG";

    /**
     * Set up the database connection.
     */
    public DbConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            this.conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC Driver.");
        } catch (SQLException e) {
            System.err.println("Error connecting to database.");
        }
    }

    /**
     * Constructor with passed connection for testing purposes.
     *
     * @param conn connection object to use
     */
    public DbConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * Getter method for database connection.
     *
     * @return database connection.
     */
    public Connection getCon() {
        return this.conn;
    }

    /**
     * Close the connection with database.
     *
     * @param pstmt preparedStatement
     * @throws SQLException throw exception when connection with db cannot be closed correctly.
     */
    public void close(PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
            if (conn != null) {
                this.conn.close();
            }
        }
    }
}