package database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DbConnectionTest {
    private transient Connection connMock;
    private transient PreparedStatement pstmtMock;

    @BeforeEach
    void setUp() {
        connMock = Mockito.mock(Connection.class);
        pstmtMock = Mockito.mock(PreparedStatement.class);
    }

    @Test
    void getConn() {
        DbConnection dbConnection = new DbConnection(connMock);

        assertEquals(connMock, dbConnection.getCon());
    }

    @Test
    void close() throws SQLException {
        DbConnection dbConnection = new DbConnection(connMock);
        dbConnection.close(pstmtMock);

        Mockito.verify(pstmtMock).close();
        Mockito.verify(connMock).close();
    }

    @Test
    void closeNullPstmt() throws SQLException {
        DbConnection dbConnection = new DbConnection(connMock);
        dbConnection.close(null);

        Mockito.verify(connMock, Mockito.never()).close();
    }

    @Test
    void closeNullConn() throws SQLException {
        DbConnection dbConnection = new DbConnection(null);
        dbConnection.close(pstmtMock);

        Mockito.verify(pstmtMock).close();
    }
}