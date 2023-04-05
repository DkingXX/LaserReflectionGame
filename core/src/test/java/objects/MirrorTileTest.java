package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import game.GridCell;
import game.TiledMapStage;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import util.Direction;

class MirrorTileTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();
    private transient GridCell gridCell;
    private transient Tile tile;
    private transient TiledMapStage stageMock;

    @BeforeEach
    void init() {
        gridCell = tileTestHelper.setupCell(Material.MIRROR);
        tile = gridCell.getTile();

        stageMock = Mockito.mock(TiledMapStage.class);
        tile.setStage(stageMock);
    }


    @ParameterizedTest
    @MethodSource("reflectionTable")
    void onLaserHit(int rotation, Direction prevDirection, Direction outcome) {
        gridCell.setRotation(rotation);
        assertEquals(outcome, tile.onLaserHit(prevDirection));
    }

    @Test
    void leftClick() {
        InputEvent evt = new InputEvent();
        evt.setButton(Input.Buttons.LEFT);

        int rot = gridCell.getRotation();

        gridCell.clicked(evt, 0, 0);

        assertTrue(gridCell.getTile() instanceof MirrorTile);
        assertEquals((rot + 3) % 4, gridCell.getRotation());

        ArgumentCaptor<TiledMapStage.StageUpdateType> updateTypeAC =
            ArgumentCaptor.forClass(TiledMapStage.StageUpdateType.class);
        Mockito.verify(stageMock).update(updateTypeAC.capture());
        assertEquals(TiledMapStage.StageUpdateType.LASERPATH, updateTypeAC.getValue());
    }

    @Test
    void rightClick() {
        Mockito.when(stageMock.getMirrorsLeft()).thenReturn(1);
        InputEvent evt = new InputEvent();
        evt.setButton(Input.Buttons.RIGHT);

        gridCell.clicked(evt, 0, 0);

        assertTrue(gridCell.getTile() instanceof AirTile);

        ArgumentCaptor<TiledMapStage.StageUpdateType> updateTypeAC =
            ArgumentCaptor.forClass(TiledMapStage.StageUpdateType.class);
        Mockito.verify(stageMock).update(updateTypeAC.capture());
        assertEquals(TiledMapStage.StageUpdateType.LASERPATH, updateTypeAC.getValue());

        Mockito.verify(stageMock).setMirrorsLeft(2);
    }

    /**
     * Helper method to test every possible combination of tile rotation and incoming laser
     * direction.
     * @return table of all combinations and the expected result.
     */
    private static Object[][] reflectionTable() {
        return new Object[][] {
            {GridCell.ROTATE_0, Direction.UP, Direction.LEFT},
            {GridCell.ROTATE_0, Direction.DOWN, Direction.NONE},
            {GridCell.ROTATE_0, Direction.LEFT, Direction.NONE},
            {GridCell.ROTATE_0, Direction.RIGHT, Direction.DOWN},

            {GridCell.ROTATE_90, Direction.UP, Direction.RIGHT},
            {GridCell.ROTATE_90, Direction.DOWN, Direction.NONE},
            {GridCell.ROTATE_90, Direction.LEFT, Direction.DOWN},
            {GridCell.ROTATE_90, Direction.RIGHT, Direction.NONE},


            {GridCell.ROTATE_180, Direction.UP, Direction.NONE},
            {GridCell.ROTATE_180, Direction.DOWN, Direction.RIGHT},
            {GridCell.ROTATE_180, Direction.LEFT, Direction.UP},
            {GridCell.ROTATE_180, Direction.RIGHT, Direction.NONE},

            {GridCell.ROTATE_270, Direction.UP, Direction.NONE},
            {GridCell.ROTATE_270, Direction.DOWN, Direction.LEFT},
            {GridCell.ROTATE_270, Direction.LEFT, Direction.NONE},
            {GridCell.ROTATE_270, Direction.RIGHT, Direction.UP}
        };
    }
}