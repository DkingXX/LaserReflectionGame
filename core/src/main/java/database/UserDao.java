package database;

import game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User object to get access to underlying database.
 */
public class UserDao {
    private transient DbConnection dataBase;

    /**
     * Instantiate new User database accessor object.
     */
    public UserDao() {
        this(new DbConnection());
    }

    /**
     * Instantiate new User database accessor object using given connection.
     *
     * @param dbConnection database connector object.
     */
    public UserDao(DbConnection dbConnection) {
        this.dataBase = dbConnection;
    }

    /**
     * Check if the program successfully connected with database.
     *
     * @return true if successfully connected with database
     */
    public boolean isConnected() {
        return this.dataBase.getCon() != null;
    }

    /**
     * Register a new player.
     *
     * @param player player
     * @return true if registration succeed.
     */
    public boolean register(Player player) {
        try {
            if (this.isConnected()) {
                String query = "INSERT INTO player VALUES(?, ?, ?)";
                PreparedStatement preparedStatement =
                        this.dataBase.getCon().prepareStatement(query);
                preparedStatement.setString(1, player.getNickname());
                preparedStatement.setString(2, player.getPassword());
                preparedStatement.setInt(3, 1);
                preparedStatement.executeUpdate();
                this.dataBase.close(preparedStatement);
                return true;
            } else {
                System.err.println("Database connection error.");
                return false;
            }

        } catch (SQLException ex) {
            //System.err.println("Player already exists");
            System.err.println(ex);
            return false;
        }
    }

    /**
     * Login a player.
     *
     * @param player player
     * @return true if login succeed.
     */
    public boolean login(Player player) {
        try {
            if (this.isConnected()) {
                String query = "SELECT * from player WHERE Nickname=? AND Password=?";
                PreparedStatement preparedStatement =
                        this.dataBase.getCon().prepareStatement(query);
                preparedStatement.setString(1, player.getNickname());
                preparedStatement.setString(2, player.getPassword());

                if (preparedStatement.executeQuery().next()) {
                    this.dataBase.close(preparedStatement);
                    return true;
                }
                this.dataBase.close(preparedStatement);
            } else {
                System.err.println("Database connection error.");

            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Method to find what level to load for the player.
     *
     * @param player the current player we want to now the level of.
     * @return the next level the player has to play. can be used in play
     *      screen to know which level to load or in high scores table.
     */
    public int getPlayerHighscore(Player player) {
        try {
            if (this.isConnected()) {
                String query = "SELECT score FROM player where Nickname=?";
                PreparedStatement preparedStatement =
                        this.dataBase.getCon().prepareStatement(query);
                preparedStatement.setString(1, player.getNickname());
                ResultSet rs = preparedStatement.executeQuery();
                try {
                    if (rs.next()) {
                        int answer = rs.getInt(1);
                        this.dataBase.close(preparedStatement);
                        return answer;
                    }
                } catch (Exception e) {
                    System.err.println(e);
                    return -1;
                } finally {
                    rs.close();
                }
                this.dataBase.close(preparedStatement);
            } else {
                System.err.println("Database connection error");
            }
            return -1;
        } catch (SQLException e) {
            System.err.println(e);
            return -1;
        }
    }

    /**
     * A method that increments the level of the player.
     * @param player the current player which won the level.
     */
    public void updateScore(Player player) {
        try {
            if (this.isConnected()) {
                String query = "UPDATE player SET Score = Score + 1 WHERE Nickname = ?";
                PreparedStatement preparedStatement =
                        this.dataBase.getCon().prepareStatement(query);
                preparedStatement.setString(1, player.getNickname());
                preparedStatement.executeUpdate();
                this.dataBase.close(preparedStatement);
            } else {
                System.err.println("Database connection error.");
            }
        } catch (SQLException ex) {
            //System.err.println("Player already exists");
            System.err.println(ex);
        }
    }

    /**
     * A method that returns top 10 players with
     *      the highest score in descending order.
     * @return a hash map of nickname score pairs.
     */
    public LinkedHashMap<String, Integer> getTop10Players() {
        try {
            if (this.isConnected()) {
                String query = "SELECT Nickname, Score FROM player ORDER BY score DESC limit 10";
                PreparedStatement preparedStatement =
                        this.dataBase.getCon().prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                try {
                    LinkedHashMap<String, Integer> hmap = new LinkedHashMap<>();

                    while (rs.next()) {
                        hmap.put(rs.getString(1), rs.getInt(2));
                    }
                    this.dataBase.close(preparedStatement);
                    return hmap;
                } catch (Exception e) {
                    System.err.println(e);
                    return null;
                } finally {
                    rs.close();
                }
            } else {
                System.err.println("Database connection error");
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
}
