package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import GameModel.Map.Tile.*;
/**
 * Created by jowens on 3/8/17.
 */
public class GameMap {

    //todo decide between these two
    //private BoardSpace[][] gameBoard;
    private HashMap<AxialCoordinate, BoardSpace> gameBoard2;
    private int numberOfTriHextiles;

    private static int boardLength = 20, boardWidth = 20;

    private List<BoardSpace> activeSpaces;

    public GameMap() {
        initializeBoard();
    }

    //using axial coordinate system, initialize the board to prepare it for play
    public void initializeBoard() {
        gameBoard2 = new HashMap<AxialCoordinate, BoardSpace>();
        BoardSpace first = new BoardSpace();
        first.setLocation(new AxialCoordinate(0,0));
        gameBoard2.put(first.getLocation(), first);
        numberOfTriHextiles = 0;
    }

    //call this whenever you place a hextile on an empty boardspace.
    public void addAdjacentBoardSpaces(AxialCoordinate initial){
        //given an axial coordinate
        //     check to see if adjacent boardspaces are active
        //          if they are not, add them to board map
        BoardSpace newBS;
        BoardSpace initialBS = gameBoard2.get(initial);

        if(gameBoard2.get(initial.getNorth()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getNorth());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setNorth(gameBoard2.get(initial.getNorth()));

        if(gameBoard2.get(initial.getNorthEast()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getNorthEast());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setNorthEast(gameBoard2.get(initial.getNorthEast()));

        if(gameBoard2.get(initial.getNorthWest()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getNorthWest());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setNorthWest(gameBoard2.get(initial.getNorthWest()));

        if(gameBoard2.get(initial.getSouth()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getSouth());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setSouth(gameBoard2.get(initial.getSouth()));
        if(gameBoard2.get(initial.getSouthEast()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getSouthEast());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setSouthEast(gameBoard2.get(initial.getSouthEast()));
        if(gameBoard2.get(initial.getSouthWest()) == null){
            newBS = new BoardSpace();
            newBS.setLocation(initial.getSouthWest());
            gameBoard2.put(newBS.getLocation(), newBS);
        }
        initialBS.setSouthWest(gameBoard2.get(initial.getSouthWest()));
    }

    public void placeFirstTile(TriHexTile first) {
        //TODO we'll probably have to deal with orienting "north" when the opposing player starts.
        BoardSpace firstBS = gameBoard2.get(new AxialCoordinate(0,0));;
        addAdjacentBoardSpaces(firstBS.getLocation());

        gameBoard2.get(firstBS.getLocation()).addTile(first.getTileOne());
        gameBoard2.get(firstBS.getNorth().getLocation()).addTile(first.getTileTwo());
        addAdjacentBoardSpaces(firstBS.getNorth().getLocation());
        gameBoard2.get(firstBS.getNorthEast().getLocation()).addTile(first.getTileThree());
        addAdjacentBoardSpaces(firstBS.getNorthEast().getLocation());

        numberOfTriHextiles++;
    }

    //TODO this currently doesn't work
    /*
     * idea:
     *  1. get legal placements above level 1
     *  2. get legal board space placements
     */
    public ArrayList<Placement> getLegalTablePlacements(TriHexTile tht) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        HexTile ht1,ht2;
        VolcanoTile ht3;

        //hextiles contained in tri-hex to be placed
        ht1 = tht.getTileOne();
        ht2 = tht.getTileTwo();
        ht3 = tht.getTileThree();

        for(BoardSpace bs: gameBoard2.values()){ //for each active board space (i.e. above a played tile, or adjacent to one)
            if(bs.isEmpty()){
                //get all placements that are possible given adjacent empty boardspaces

                //NORTH AND NORTHEAST
                boolean northLegal, northEastLegal, southEastLegal, southLegal, southWestLegal, northWestLegal;

                northLegal = legalLevel0(bs.getNorth().getLocation());
                northEastLegal = legalLevel0(bs.getNorthEast().getLocation());
                northWestLegal = legalLevel0(bs.getNorthWest().getLocation());
                southLegal = legalLevel0(bs.getSouth().getLocation());
                southEastLegal = legalLevel0(bs.getSouthEast().getLocation());
                southWestLegal = legalLevel0(bs.getSouthWest().getLocation());

                if(northLegal && northEastLegal) {
                    Placement p1 = new Placement(bs, bs.getNorth(), bs.getNorthEast(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getNorthEast(), bs, bs.getNorth(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getNorth(), bs.getNorthEast(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(northEastLegal && southEastLegal) {
                    Placement p1 = new Placement(bs, bs.getNorthEast(), bs.getSouthEast(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouthEast(), bs, bs.getNorthEast(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getNorthEast(), bs.getSouthEast(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southEastLegal && southLegal) {
                    Placement p1 = new Placement(bs, bs.getSouthEast(), bs.getSouth(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouth(), bs, bs.getSouthEast(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouthEast(), bs.getSouth(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southLegal && southWestLegal) {
                    Placement p1 = new Placement(bs, bs.getSouth(), bs.getSouthWest(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouthWest(), bs, bs.getSouth(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouth(), bs.getSouthWest(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southWestLegal && northWestLegal) {
                    Placement p1 = new Placement(bs, bs.getSouthWest(), bs.getNorthWest(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getNorthWest(), bs, bs.getSouthWest(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouthWest(), bs.getNorthWest(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(northWestLegal && northLegal) {
                    Placement p1 = new Placement(bs, bs.getNorthWest(), bs.getNorth(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getNorth(), bs, bs.getNorthWest(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getNorthWest(), bs.getNorth(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                //END NORTH AND NORTHEAST
            }
        }
        return returnMe;
    }

    private boolean legalLevel0(AxialCoordinate ac) {
        BoardSpace BStoTest = gameBoard2.get(ac);
        boolean northExists = !(BStoTest == null);
        boolean isBoardLevel = true;
        if(northExists){
            if(BStoTest.getLevel() >0){
                isBoardLevel = false;
            }
        }
        if( BStoTest == null || isBoardLevel) { //north tile is legal
            return true;
        }
        else {
            return false;
        }
    }
    /*  TODO
        for each of the three hextiles in the tri-hex, attempt to place them at the placeAt tile, and then check each rotation with that tile
        as center to see if such rotations are legal.
     */
    public ArrayList<Placement> getLegalPlacementsAtHexTile(TriHexTile toBePlaced, HexTile placeAt){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();

        HexTile ht1,ht2,ht3;

        ht1 = toBePlaced.getTileOne();
        ht2 = toBePlaced.getTileTwo();
        ht3 = toBePlaced.getTileThree();

        if(placeAt.terrainType() == TerrainType.VOLCANO){ //bombs away
            BoardSpace mine = placeAt.getBoardSpace();// law of demeter violation
            BoardSpace north = mine.getNorth();
            BoardSpace northEast = mine.getNorthEast();
            BoardSpace northWest = mine.getNorthWest();
            BoardSpace south = mine.getSouth();
            BoardSpace southEast = mine.getSouthEast();
            BoardSpace southWest = mine.getSouthWest();


            if(north.getLevel() == mine.getLevel()){ //me, north, northeast
                if(northEast.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, north,northEast, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(northEast.getLevel() == mine.getLevel()){
                if(southEast.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, northEast,southEast, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(southEast.getLevel() == mine.getLevel()){
                if(south.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, southEast,south, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(south.getLevel() == mine.getLevel()){
                if(southWest.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, south,southWest, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(southWest.getLevel() == mine.getLevel()){
                if(northWest.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, southWest,northWest, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(northWest.getLevel() == mine.getLevel()){
                if(north.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, northWest,north, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }

        }
        else{ //only allow volcano tile placements on top of of other tiles
            return null;
        }

        return null;
    }

    /*
     * All legal plaaements > 0
     */
    public ArrayList<Placement> getLegalMapPlacements(TriHexTile tht){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        for(BoardSpace bs: gameBoard2.values()){
            HexTile thisTile = bs.topTile();
            if(thisTile != null){
                if(thisTile.terrainType() == TerrainType.VOLCANO)
                    returnMe.addAll(getLegalPlacementsAtHexTile(tht, thisTile));
            }
        }
        return returnMe;
    }
    /*
     * returns all triplets of adjacent tiles that aren't all in the same tri-hex tile
     * TODO this would be useful for ignoring illegal placements
     */
//    public List<TriHexTile> sameLevelAdjacentHexesOfDifferentTriHexes() {
//        List<HexTile> returnMe;
//        for
//    }

    public ArrayList<Placement> getAllLegalPlacements(TriHexTile tht){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        returnMe.addAll(getLegalMapPlacements(tht));
        returnMe.addAll(getLegalTablePlacements(tht));
        return returnMe;
    }

    /*
        takes a placement object and implements it's effects on the board
     */
    public void implementPlacement(Placement p){
        p.place();
    }

    public boolean isLegalPlacement(Placement p) {
        if(p.isLevelPlacement() && p.tilesAreOfProperType()){
            return true;
        }
        else
            return false;
    }

    /*
        returns a list of all non-null, top-level hextiles on the board (i.e. tiles that have been placed)
     */
    public ArrayList<HexTile> getVisible() {
        ArrayList<HexTile> visible =new ArrayList<HexTile>();
        Collection<BoardSpace> bs = gameBoard2.values();
        for(BoardSpace b: bs){
            if(!b.isEmpty())
                visible.add(b.topTile());
        }
        return visible;
    }

    public void printInfoAboutMap(){
        for(BoardSpace bs: gameBoard2.values()){
            if(bs.topTile() != null)
                System.out.println(bs.topTile() + " " + bs.getLocation());
        }
    }

    public int getNumberOfTriHextiles(){
        return numberOfTriHextiles;
    }

    /*
        given a hextile, do a BFS or DFS for all contiguos, connected tiles of the same type and return them as a list
     */
    public ArrayList<HexTile> getContiguousTerrainFromTile(TerrainTile tt){
        return null; //TODO
    }
}
