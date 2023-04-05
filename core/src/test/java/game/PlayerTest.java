package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void playerConstructor() {
        Player player = new Player("player1", "passw0rd");

        assertEquals("player1", player.getNickname());
        assertEquals("passw0rd", player.getPassword());
    }

    @Test
    void playerSetters() {
        Player player = new Player("player1", "passw0rd");
        player.setNickname("gentstudent");
        player.setPassword("verystr0nk!");


        assertEquals("gentstudent", player.getNickname());
        assertEquals("verystr0nk!", player.getPassword());
    }
}