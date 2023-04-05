package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

class BombTileTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();
    private transient GridCell gridCell;
    private transient Tile tile;
    private transient TiledMapStage stageMock;

    @BeforeEach
    void init() {
        gridCell = tileTestHelper.setupCell(Material.BOMB);
        tile = gridCell.getTile();

        stageMock = Mockito.mock(TiledMapStage.class);
        tile.setStage(stageMock);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void onLaserHit(Direction prevDirection) {
        // every incoming direction gets blocked.
        assertEquals(Direction.NONE, tile.onLaserHit(prevDirection));
    }

    @Test
    void explosion() {
        tile.onLaserHit(Direction.UP);

        ArgumentCaptor<TiledMapStage.StageUpdateType> updateTypeAC =
            ArgumentCaptor.forClass(TiledMapStage.StageUpdateType.class);
        Mockito.verify(stageMock).update(updateTypeAC.capture());

        assertEquals(TiledMapStage.StageUpdateType.LOSE, updateTypeAC.getValue());
    }

    @Test
    void click() {
        // when a bomb gets clicked, nothing happens.
        tile.clicked(new InputEvent(), 0, 0);

        Mockito.verifyNoInteractions(stageMock);
    }
}