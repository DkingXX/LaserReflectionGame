package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import game.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.InstanceOf;

class UserDaoTest {

    private static final String PARAMETERIZED_SETUP = "{index} => username={0}, password={1}";
    private static final String USER_PASS_CSV = "RoetVeegPiet69, s1nterklaa$";

    private transient DbConnection dbConnectionSpy;
    private transient Connection connectionMock;
    private transient UserDao userDao;
    private transient ResultSet resultSetMock;

    @BeforeEach
    void mockDb() {
        connectionMock = Mockito.mock(Connection.class);
        dbConnectionSpy = Mockito.spy(new DbConnection(connectionMock));
        userDao = new UserDao(dbConnectionSpy);
    }


    @Test
    void connected() {
        assertTrue(userDao.isConnected());
    }

    private void disconnectUserDao() {
        DbConnection dbNoConnection = new DbConnection(null);
        userDao = new UserDao(dbNoConnection);
    }

    @Test
    void notConnected() {
        disconnectUserDao();
        assertFalse(userDao.isConnected());
    }

    PreparedStatement mockPreparedStatement() throws SQLException {
        PreparedStatement preparedStatementMock = Mockito.mock(PreparedStatement.class);
        when(connectionMock.prepareStatement(Mockito.anyString()))
            .thenReturn(preparedStatementMock);

        return preparedStatementMock;
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void register(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();

        assertTrue(userDao.register(new Player(username, password)));

        Mockito.verify(preparedStatementMock).setString(1, username);
        Mockito.verify(preparedStatementMock).setString(2, password);
        Mockito.verify(preparedStatementMock).setInt(3, 1);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(connectionMock).prepareStatement(queryCaptor.capture());
        assertEquals("INSERT INTO player VALUES(?, ?, ?)", queryCaptor.getValue());
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void registrationFail(String username, String password) throws SQLException {
        mockPreparedStatement();
        Mockito.doThrow(new SQLException()).when(dbConnectionSpy)
            .close(Mockito.nullable(PreparedStatement.class));

        assertFalse(userDao.register(new Player(username, password)));
    }

    @Test
    void registrationNoConnection() {
        disconnectUserDao();

        assertFalse(userDao.register(new Player("Thomas", "waanzin")));
    }

    private void mockResultSet(PreparedStatement preparedStatementMock, boolean hasNext)
        throws SQLException {
        resultSetMock = Mockito.mock(ResultSet.class);
        when(resultSetMock.next()).thenReturn(hasNext);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void login(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);

        assertTrue(userDao.login(new Player(username, password)));

        Mockito.verify(preparedStatementMock).setString(1, username);
        Mockito.verify(preparedStatementMock).setString(2, password);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(connectionMock).prepareStatement(queryCaptor.capture());
        assertEquals("SELECT * from player WHERE Nickname=? AND Password=?",
            queryCaptor.getValue());
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void loginFail(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, false);

        assertFalse(userDao.login(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void loginFailExceptionDbConn(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);
        Mockito.doThrow(new SQLException()).when(dbConnectionSpy)
            .close(Mockito.nullable(PreparedStatement.class));

        assertFalse(userDao.login(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
        USER_PASS_CSV
    })
    void loginNoConnection(String username, String password) {
        disconnectUserDao();

        assertFalse(userDao.login(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void getScore(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);

        when(resultSetMock.getInt(anyInt())).thenReturn(1);

        userDao.register(new Player(username, password));
        assertEquals(1, userDao.getPlayerHighscore(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void getScoreNoConnection(String username, String password) {
        disconnectUserDao();

        assertEquals(-1, userDao.getPlayerHighscore(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void getScoreException(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);

        Mockito.doThrow(new SQLException()).when(dbConnectionSpy)
                .close(Mockito.nullable(PreparedStatement.class));

        assertEquals(-1, userDao.getPlayerHighscore(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void getScoreFail(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, false);

        assertEquals(-1, userDao.getPlayerHighscore(new Player(username, password)));
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void updateScore(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        userDao.updateScore(new Player(username, password));
        verify(preparedStatementMock).executeUpdate();
    }

    @ParameterizedTest(name = PARAMETERIZED_SETUP)
    @CsvSource({
            USER_PASS_CSV
    })
    void updateScoreNoConnection(String username, String password) throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        disconnectUserDao();
        userDao.updateScore(new Player(username, password));
        verify(preparedStatementMock, times(0)).executeUpdate();
    }


    @Test
    void getTop10Players() throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);

        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(1)).thenReturn("Liudas");
        when(resultSetMock.getInt(2)).thenReturn(1);

        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("Liudas", 1);

        assertEquals(expected, userDao.getTop10Players());
    }

    @Test
    void resultSetException() throws SQLException {
        PreparedStatement preparedStatementMock = mockPreparedStatement();
        mockResultSet(preparedStatementMock, true);

        when(resultSetMock.next()).thenThrow(new SQLException());

        assertNull(userDao.getTop10Players());
    }

    @Test
    void getTop10PlayersNoConnection() {
        disconnectUserDao();

        assertNull(userDao.getTop10Players());
    }

    @Test
    void getTop10PlayersException() {

    }
}