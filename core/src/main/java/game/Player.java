package game;

/**
 * The player class contains the players nickname and password.
 */
public class Player {

    private String nickname;
    private String password;

    /**
     * Initialises a new Player instance.
     * @param nickname The nickname of the player
     * @param password the password of the player
     */
    public Player(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    /**
     * Gets the nickname of the player.
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the player.
     * @param nickname the nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the password of the player.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the player.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
