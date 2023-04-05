package game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Integration testing for the TiledMapInputListener.
 */
class TiledMapInputListenerTest {

    private transient GridCell cellMock;

    /**
     * Sets up all the mocks.
     */
    @BeforeEach
    public void setUp() {
        cellMock = Mockito.mock(GridCell.class);
    }

    /**
     * Tests the constructor method.
     */
    @Test
    public void constructorTest() {
        TiledMapInputListener tiledMapInputListener = new TiledMapInputListener(cellMock);
        Assertions.assertEquals(cellMock, tiledMapInputListener.getCell());
    }

    /**
     * Tests the clicked method.
     */
    @Test
    public void testClicked() {
        InputEvent eventMock = Mockito.mock(InputEvent.class);
        TiledMapInputListener tiledMapInputListener = new TiledMapInputListener(cellMock);

        tiledMapInputListener.clicked(eventMock, 2, 2);

        Mockito.verify(cellMock, Mockito.times(1)).clicked(eventMock, 2,2);
    }
}

