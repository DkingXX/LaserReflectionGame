//package game;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TiledMapTile;
//import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
//import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//public class TiledMapActorTest {
//    private transient TiledMap tiledMapMock;
//    private transient TiledMapTileSets tiledMapTileSetsMock;
//    private transient TiledMapTileLayer tiledMapTileLayerMock;
//
//    @BeforeEach
//    void setupMocks() {
//        tiledMapTileSetsMock = Mockito.mock(TiledMapTileSets.class);
//
//        tiledMapMock = Mockito.mock(TiledMap.class);
//        Mockito.when(tiledMapMock.getTileSets()).thenReturn(tiledMapTileSetsMock);
//
//        tiledMapTileLayerMock = Mockito.mock(TiledMapTileLayer.class);
//    }
//
//    /**
//     * Mocks a TiledMapTile such that tile.getId() returns the given id.
//     *
//     * @param id the id of the tile type.
//     * @return the mocked tile.
//     */
//    TiledMapTile mockTile(int id) {
//        TiledMapTile tileMock = Mockito.mock(TiledMapTile.class);
//        Mockito.when(tileMock.getId()).thenReturn(id);
//
//        return tileMock;
//    }
//
//    /**
//     * Constructs a TiledMapTileLayer.Cell such that cell.getTile().getId() returns the given id.
//     *
//     * @param id the id of the tile type the cell contains.
//     * @return the cell.
//     */
//    TiledMapTileLayer.Cell mockCell(int id) {
//        TiledMapTileLayer.Cell cellMock = new TiledMapTileLayer.Cell();
//        cellMock.setTile(mockTile(id));
//
//        return cellMock;
//    }
//
//    @Test
//    void getTypeBrick() {
//        TiledMapTileLayer.Cell brickCell = mockCell(17);
//
//        TiledMapActor brickActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            brickCell);
//
//        assertEquals(TiledMapActor.TileTypes.BRICK, brickActor.getType());
//    }
//
//    @Test
//    void getTypeMirror() {
//        TiledMapTileLayer.Cell mirrorCell = mockCell(71);
//
//        TiledMapActor mirrorActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            mirrorCell);
//
//        assertEquals(TiledMapActor.TileTypes.MIRROR, mirrorActor.getType());
//    }
//
//    @Test
//    void getTypeAir() {
//        TiledMapTileLayer.Cell airCell = mockCell(96);
//
//        TiledMapActor airActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            airCell);
//
//        assertEquals(TiledMapActor.TileTypes.AIR, airActor.getType());
//    }
//
//    @Test
//    void getTypeNullAir() {
//        TiledMapActor mirrorActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            null);
//
//        assertEquals(TiledMapActor.TileTypes.AIR, mirrorActor.getType());
//    }
//
//    @Test
//    void setAir() {
//        TiledMapTile airTileMock = mockTile(96);
//        Mockito.when(tiledMapTileSetsMock.getTile(96)).thenReturn(airTileMock);
//        TiledMapTileLayer.Cell mirrorCell = mockCell(71);
//
//        TiledMapActor mirrorActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            mirrorCell);
//
//        mirrorActor.setAir();
//
//        Mockito.verify(tiledMapTileSetsMock).getTile(96);
//        assertEquals(TiledMapActor.TileTypes.AIR, mirrorActor.getType());
//    }
//
//    @Test
//    void setMirror() {
//        TiledMapTile mirrorTileMock = mockTile(71);
//        Mockito.when(tiledMapTileSetsMock.getTile(71)).thenReturn(mirrorTileMock);
//        TiledMapTileLayer.Cell airCell = mockCell(96);
//
//        TiledMapActor mirrorActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            airCell);
//
//        mirrorActor.setMirror();
//
//        Mockito.verify(tiledMapTileSetsMock).getTile(71);
//        assertEquals(TiledMapActor.TileTypes.MIRROR, mirrorActor.getType());
//    }
//
//    @Test
//    void rotate() {
//        TiledMapTileLayer.Cell mirrorCell = mockCell(71);
//
//        TiledMapActor mirrorActor = new TiledMapActor(tiledMapMock, tiledMapTileLayerMock,
//            mirrorCell);
//
//
//        assertEquals(0, mirrorCell.getRotation());
//        mirrorActor.rotate();
//        assertEquals(1, mirrorCell.getRotation());
//        mirrorActor.rotate();
//        assertEquals(2, mirrorCell.getRotation());
//        mirrorActor.rotate();
//        assertEquals(3, mirrorCell.getRotation());
//        mirrorActor.rotate();
//        assertEquals(0, mirrorCell.getRotation());
//    }
//}
