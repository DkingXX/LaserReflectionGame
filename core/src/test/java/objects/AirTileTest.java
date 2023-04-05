package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import game.GridCell;
import game.TiledMapStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import util.Direction;

class AirTileTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();
    private transient GridCell gridCell;
    private transient Tile tile;
    private transient TiledMapStage stageMock;

    @BeforeEach
    void init() {
        gridCell = tileTestHelper.setupCell(Material.AIR);
        tile = gridCell.getTile();

        stageMock = Mockito.mock(TiledMapStage.class);
        tile.setStage(stageMock);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void onLaserHit(Direction prevDirection) {
        assertEquals(prevDirection, tile.onLaserHit(prevDirection));
    }

    @Test
    void leftClickWithMirrorsLeft() {
        Mockito.when(stageMock.getMirrorsLeft()).thenReturn(1);

        InputEvent evt = new InputEvent();
        evt.setButton(Input.Buttons.LEFT);

        gridCell.clicked(evt, 0, 0);

        assertTrue(gridCell.getTile() instanceof MirrorTile);

        ArgumentCaptor<TiledMapStage.StageUpdateType> updateTypeAC =
            ArgumentCaptor.forClass(TiledMapStage.StageUpdateType.class);
        Mockito.verify(stageMock).update(updateTypeAC.capture());
        assertEquals(TiledMapStage.StageUpdateType.LASERPATH, updateTypeAC.getValue());

        Mockito.verify(stageMock).setMirrorsLeft(0);
    }

    @Test
    void leftClickNoMirrorsLeft() {
        Mockito.when(stageMock.getMirrorsLeft()).thenReturn(0);

        InputEvent evt = new InputEvent();
        evt.setButton(Input.Buttons.LEFT);

        gridCell.clicked(evt, 0, 0);

        assertTrue(gridCell.getTile() instanceof AirTile);
    }

    @Test
    void rightClick() {
        InputEvent evt = new InputEvent();
        evt.setButton(Input.Buttons.RIGHT);

        gridCell.clicked(evt, 0, 0);

        assertTrue(gridCell.getTile() instanceof AirTile);
    }
}