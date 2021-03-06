package GameModel.Map;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import org.junit.*;

/**
 * Created by Z_K on 3/24/2017.
 */
public class BoardSpaceTest {
    private static GameMap map;
    private static OffsetCoordinate location;

    @Before
    public void initializeTest(){
        map = new GameMap();
        location = new OffsetCoordinate(0, 0);
    }

    @Test
    public void createABoardSpaceTest(){
        BoardSpace boardSpace = new BoardSpace(location,map);
        Assert.assertFalse(boardSpace.hasTile());
        Assert.assertTrue(boardSpace.getLocation().compare(location));
    }

    @Test
    public void activateAdjacentBoardSpaceTest(){
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());

        map.implementPlacement(map.getAllLegalPlacements(tht).get(0));

        BoardSpace center = map.getVisibleAtAxialCoordinate(new OffsetCoordinate(0,0)).getBoardSpace();

        Assert.assertTrue(center.getNorth().isActive());
        Assert.assertTrue(center.getNorthEast().isActive());
        Assert.assertTrue(center.getNorthWest().isActive());
        Assert.assertTrue(center.getSouth().isActive());
        Assert.assertTrue(center.getSouthEast().isActive());
        Assert.assertTrue(center.getSouthWest().isActive());
    }

    @Test
    public void levelUpTest(){
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        map.implementPlacement(map.getAllLegalPlacements(tht).get(0));
        BoardSpace center = map.getVisibleAtAxialCoordinate(new OffsetCoordinate(0,0)).getBoardSpace();
        int centerLevelBefore = center.getLevel();
        HexTile grass = new Grass();
        center.addTile(grass);
        int centerLevelAfter = center.getLevel();
        Assert.assertNotEquals(centerLevelBefore, centerLevelAfter);
    }

    @After
    public void cleanUp(){

    }

}
