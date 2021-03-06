package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Contiguous.ContiguousTerrainTypeTiles;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.*;

import java.util.*;

/**
 * Created by jowens on 3/8/17.
 */
public class GameMap {

    private HashMap<OffsetCoordinate, BoardSpace> gameBoard;
    private ArrayList<TriHexTile> playedTriHexTiles = new ArrayList<TriHexTile>();
    private int numberOfTriHextiles;
    private boolean firstTurn = true;

    public GameMap() {
        initializeBoard();
    }

    //using axial coordinate system, initialize the board to prepare it for play
    public void initializeBoard() {
        gameBoard = new HashMap<OffsetCoordinate, BoardSpace>();
        OffsetCoordinate originLocation = new OffsetCoordinate(0, 0);
        BoardSpace originBS = this.addBoardSpace(originLocation);
        originBS.activate();
        this.addRadialBoardSpaces(2,originBS);
        this.connectAdjacentBoardSpaces(originBS);
        this.activateAdjacentBoardSpaces(originBS);

        this.connectAdjacentBoardSpaces(originBS.getNorth());
        this.connectAdjacentBoardSpaces(originBS.getNorthEast());
        this.connectAdjacentBoardSpaces(originBS.getNorthWest());
        this.connectAdjacentBoardSpaces(originBS.getSouth());
        this.connectAdjacentBoardSpaces(originBS.getSouthWest());
        this.connectAdjacentBoardSpaces(originBS.getSouthEast());

        placeStartingTile();

        numberOfTriHextiles = 0;

        for (BoardSpace bs : gameBoard.values()) {
            connectAdjacentBoardSpaces(bs);
        }
    }

    private void placeStartingTile(){
        VolcanoTile volcano = new VolcanoTile();
        TriHexTile topPart = new TriHexTile(new Jungle(), new Lake(), volcano);
        TriHexTile bottomPart = new TriHexTile(new Grass(), new Rock(), volcano);
        topPart.getTileOne().setTriHexTile(bottomPart);
        topPart.getTileTwo().setTriHexTile(bottomPart);

        BoardSpace center = gameBoard.get(new OffsetCoordinate(0, 0));
        BoardSpace northWest = gameBoard.get(new OffsetCoordinate(0, -2));
        BoardSpace southWest = gameBoard.get(new OffsetCoordinate(-1, 1));
        BoardSpace northEast = gameBoard.get(new OffsetCoordinate(1, -1));
        BoardSpace southEast = gameBoard.get(new OffsetCoordinate(0, 2));
        Placement topPlacement = new Placement(northWest, northEast, center, topPart.getTileOne(), topPart.getTileTwo(), volcano, 1);
        Placement bottomPlacement = new Placement(southEast, southWest, center, bottomPart.getTileOne(), bottomPart.getTileTwo(), volcano, 4);
        implementPlacement(topPlacement);
        implementPlacement(bottomPlacement);
        center.removeTopTile();
        volcano.setLevel(1);
    }

    //call this whenever you place a hextile on an empty boardspace.
    public void addAdjacentBoardSpaces(OffsetCoordinate initial) {
        //given an axial coordinate
        //     check to see if adjacent boardspaces are active
        //          if they are not, add them to board map
        BoardSpace newBS;
        BoardSpace initialBS = gameBoard.get(initial);

        if (gameBoard.get(initial.getNorth()) == null) { //north
            this.addBoardSpace(initial.getNorth());
        }
        initialBS.setNorth(gameBoard.get(initial.getNorth()));

        if (gameBoard.get(initial.getNorthEast()) == null) {
            this.addBoardSpace(initial.getNorthEast());
        }
        initialBS.setNorthEast(gameBoard.get(initial.getNorthEast()));

        if (gameBoard.get(initial.getNorthWest()) == null) {
            this.addBoardSpace(initial.getNorthWest());
        }
        initialBS.setNorthWest(gameBoard.get(initial.getNorthWest()));

        if (gameBoard.get(initial.getSouth()) == null) {
            this.addBoardSpace(initial.getSouth());
        }
        initialBS.setSouth(gameBoard.get(initial.getSouth()));
        if (gameBoard.get(initial.getSouthEast()) == null) {
            this.addBoardSpace(initial.getSouthEast());
        }
        initialBS.setSouthEast(gameBoard.get(initial.getSouthEast()));
        if (gameBoard.get(initial.getSouthWest()) == null) {
            this.addBoardSpace(initial.getSouthWest());
        }
        initialBS.setSouthWest(gameBoard.get(initial.getSouthWest()));
    }

    private void connectTwoBoardSpaces(BoardSpace bs1, BoardSpace bs2, Direction bs1TObs2) {
        switch (bs1TObs2) {
            case NORTH:
                bs1.setNorth(bs2);
                bs2.setSouth(bs1);
                break;
            case NORTHEAST:
                bs1.setNorthEast(bs2);
                bs2.setSouthWest(bs1);
                break;
            case SOUTHEAST:
                bs1.setSouthEast(bs2);
                bs2.setNorthWest(bs1);
                break;
            case SOUTH:
                bs1.setSouth(bs2);
                bs2.setNorth(bs1);
                break;
            case SOUTHWEST:
                bs1.setSouthWest(bs2);
                bs2.setNorthEast(bs1);
                break;
            case NORTHWEST:
                bs1.setNorthWest(bs2);
                bs2.setSouthEast(bs1);
                break;
        }
    }

    public void connectAdjacentBoardSpaces(BoardSpace initial) {
        OffsetCoordinate initialLocation = initial.getLocation();

        BoardSpace north = gameBoard.get(initialLocation.getNorth());
        BoardSpace northEast = gameBoard.get(initialLocation.getNorthEast());
        BoardSpace northWest = gameBoard.get(initialLocation.getNorthWest());
        BoardSpace south = gameBoard.get(initialLocation.getSouth());
        BoardSpace southEast = gameBoard.get(initialLocation.getSouthEast());
        BoardSpace southWest = gameBoard.get(initialLocation.getSouthWest());

        if (north != null)
            connectTwoBoardSpaces(initial, north, Direction.NORTH);
        if (northEast != null)
            connectTwoBoardSpaces(initial, northEast, Direction.NORTHEAST);
        if (northWest != null)
            connectTwoBoardSpaces(initial, northWest, Direction.NORTHWEST);
        if (south != null)
            connectTwoBoardSpaces(initial, south, Direction.SOUTH);
        if (southEast != null)
            connectTwoBoardSpaces(initial, southEast, Direction.SOUTHEAST);
        if (southWest != null)
            connectTwoBoardSpaces(initial, southWest, Direction.SOUTHWEST);
    }

//    public void placeFirstTile(TriHexTile first) {
//        System.out.println("placing first tile");
//        //TODO we'll probably have to deal with orienting "north" when the opposing player starts.
//        BoardSpace firstBS = gameBoard.get(new OffsetCoordinate(0,0));;
//        addRadialBoardSpaces(2, firstBS);
//
//        BoardSpace current = gameBoard.get(firstBS.getLocation());
//        current.addTile(first.getTileOne());
//        first.getTileOne().setBoardSpace(current);
//        current = gameBoard.get(firstBS.getLocation().getNorth());
//        current.addTile(first.getTileTwo());
//        first.getTileTwo().setBoardSpace(current);
//        current = gameBoard.get(firstBS.getLocation().getNorthEast());
//        current.addTile(first.getTileThree());
//
//        //TODO does this work?
//        addRadialBoardSpaces(2, firstBS);
//        addRadialBoardSpaces(2, gameBoard.get(firstBS.getLocation().getNorth()));
//        addRadialBoardSpaces(2, gameBoard.get(firstBS.getLocation().getNorthEast()));
//
//        activateAdjacentBoardSpaces(gameBoard.get(firstBS.getLocation()));
//        activateAdjacentBoardSpaces(gameBoard.get(firstBS.getLocation().getNorth()));
//        activateAdjacentBoardSpaces(gameBoard.get(firstBS.getLocation().getNorthEast()));
//
//        numberOfTriHextiles++;
//    }

    private void activateAdjacentBoardSpaces(BoardSpace center) {
        BoardSpace north = gameBoard.get(center.getLocation().getNorth());
        BoardSpace northEast = gameBoard.get(center.getLocation().getNorthEast());
        BoardSpace northWest = gameBoard.get(center.getLocation().getNorthWest());
        BoardSpace south = gameBoard.get(center.getLocation().getSouth());
        BoardSpace southEast = gameBoard.get(center.getLocation().getSouthEast());
        BoardSpace southWest = gameBoard.get(center.getLocation().getSouthWest());

        southWest.activate(center, Direction.NORTHEAST);
        southEast.activate(center, Direction.NORTHWEST);
        south.activate(center, Direction.NORTH);
        northWest.activate(center, Direction.SOUTHEAST);
        northEast.activate(center, Direction.SOUTHWEST);
        north.activate(center, Direction.SOUTH);

//        connectAdjacentBoardSpaces(north);
//        connectAdjacentBoardSpaces(northEast);
//        connectAdjacentBoardSpaces(northWest);
//        connectAdjacentBoardSpaces(south);
//        connectAdjacentBoardSpaces(southWest);
//        connectAdjacentBoardSpaces(southEast);
    }

    public void addRadialBoardSpaces(int radius, BoardSpace origin) {
        if (radius < 0)
            return;

        // spacesInRadius is the list of all bses being added, or found
        List<BoardSpace> spacesInRadius = new ArrayList<BoardSpace>();

        // queue will keep track of which BoardSpace to BFS out of next
        Queue<Distance> queue = new LinkedList<Distance>();

        // visited keeps track of which BoardSpaces we have already visited
        HashSet<Distance> visited = new HashSet<Distance>();

        // add the center BoardSpace to spacesInRadius and visited
        spacesInRadius.add(origin);
        //visited.add(origin);
        queue.add(new Distance(0, origin));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();
            if (current.getDistance() >= radius || visited.contains(current)) //if we've reached the radius or we've already visited, skip
                continue;

            // iterate through all of the current Tile's neighbors
            for (Direction direction : Direction.values()) {
                BoardSpace bs = current.getBoardSpace();
                OffsetCoordinate location = bs.getLocation();
                OffsetCoordinate neighborLocation = location.getByDirection(direction);
                BoardSpace neighbor = gameBoard.get(neighborLocation);
                // if the Tile is on the map and we haven't visited it already
                if (neighbor == null) {
                    neighbor = addBoardSpace(neighborLocation);
                }

                connectAdjacentBoardSpaces(neighbor);
                queue.add(new Distance(current.getDistance() + 1, neighbor));
            }
            visited.add(current);
        }
    }

    public HexTile getHexTileAt(OffsetCoordinate location) {
        return getBoardSpaceAt(location).topTile();
    }

    public BoardSpace getBoardSpaceAt(OffsetCoordinate location) { return gameBoard.get(location); }

    /*
     CALL THIS INSTEAD OF DIRECTLY MODIFYING GAMEBOARD2
     */
    public BoardSpace addBoardSpace(OffsetCoordinate ac) {
        BoardSpace newBS = new BoardSpace(ac, this);
        gameBoard.put(ac, newBS);
        return newBS;
    }

    //TODO this currently doesn't work
    /*
     * idea:
     *  1. get legal placements above level 1
     *  2. get legal board space placements
     */
    public ArrayList<Placement> getLegalTablePlacements(TriHexTile tht) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        HexTile ht1, ht2;
        VolcanoTile ht3;


        //hextiles contained in tri-hex to be placed
        ht1 = tht.getTileOne();
        ht2 = tht.getTileTwo();
        ht3 = tht.getTileThree();

        firstTurn = false;
//
        for (BoardSpace bs : gameBoard.values()) { //for each active board space (i.e. above a played tile, or adjacent to one)
            OffsetCoordinate location = bs.getLocation();
            BoardSpace north = gameBoard.get(location.getNorth());
            BoardSpace northeast = gameBoard.get(location.getNorthEast());
            BoardSpace northwest = gameBoard.get(location.getNorthWest());
            BoardSpace south = gameBoard.get(location.getSouth());
            BoardSpace southeast = gameBoard.get(location.getSouthEast());
            BoardSpace southwest = gameBoard.get(location.getSouthWest());

            if (bs.isEmpty() && bs.isActive()) {
                //get all placements that are possible given adjacent empty boardspaces

                //NORTH AND NORTHEAST
                boolean northLegal, northEastLegal, southEastLegal, southLegal, southWestLegal, northWestLegal;


                northLegal = legalLevel0(north.getLocation());
                northEastLegal = legalLevel0(northeast.getLocation());
                northWestLegal = legalLevel0(northwest.getLocation());
                southLegal = legalLevel0(south.getLocation());
                southEastLegal = legalLevel0(southeast.getLocation());
                southWestLegal = legalLevel0(southwest.getLocation());


                if (northLegal && northEastLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, north, northeast, ht1, ht2, ht3, 1));
                }
                if (northEastLegal && southEastLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, northeast, southeast, ht1, ht2, ht3, 2));
                }
                if (southEastLegal && southLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, southeast, south, ht1, ht2, ht3, 3));
                }
                if (southLegal && southWestLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, south, southwest, ht1, ht2, ht3, 4));
                }
                if (southWestLegal && northWestLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, southwest, northwest, ht1, ht2, ht3, 5));
                }
                if (northWestLegal && northLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, northwest, north, ht1, ht2, ht3, 6));
                }
            }
        }

        return returnMe;
    }

    public ArrayList<Placement> getAllPlacementsAtThreeBoardSpaces(BoardSpace b1, BoardSpace b2, BoardSpace b3, HexTile ht1, HexTile ht2, HexTile ht3, int orientation) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        int orientation1 = orientation + 4;
        int orientation2 = orientation + 2;
        int orientation3 = orientation;
        if(orientation1 > 6){
            orientation1 -= 6;
        }
        if(orientation2 > 6){
            orientation2 -= 6;
        }
        Placement p1 = new Placement(b1, b2, b3, ht1, ht2, ht3, orientation1); // orientation 1
        Placement p2 = new Placement(b3, b1, b2, ht1, ht2, ht3, orientation2); // orientation 1
        Placement p3 = new Placement(b2, b3, b1, ht1, ht2, ht3, orientation3); // oritentation 1

        returnMe.add(p1);
        returnMe.add(p2);
        returnMe.add(p3);
        return returnMe;
    }

    protected boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * tests that
     * A. the board space exists in the gameBoard (at the give coordiate)
     * B. The Board Space is level 0
     */
    private boolean legalLevel0(OffsetCoordinate ac) {
        BoardSpace BStoTest = gameBoard.get(ac);
        return BStoTest.isEmpty() && BStoTest.getLevel() == 0;
    }


    /**  TODO
        for each of the three hextiles in the tri-hex, attempt to place them at the placeAt tile, and then check each rotation with that tile
        as center to see if such rotations are legal.
     */
    private ArrayList<Placement> getLegalPlacementsAtHexTile(TriHexTile toBePlaced, HexTile placeAt) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();

        VolcanoTile volcanoTile = toBePlaced.getVolcanoTile();
        TerrainTile clockwiseNonVolcanoTile = toBePlaced.getClockwiseNonVolcanoTile();
        TerrainTile counterClockwiseNonVolcanoTile = toBePlaced.getCounterClockwiseNonVolcanoTile();
        
        if(placeAt.terrainType() == TerrainType.VOLCANO){ //bombs away
            BoardSpace mine = placeAt.getBoardSpace();
            BoardSpace north = mine.getNorth();
            BoardSpace northEast = mine.getNorthEast();
            BoardSpace southEast = mine.getSouthEast();
            BoardSpace south = mine.getSouth();
            BoardSpace southWest = mine.getSouthWest();
            BoardSpace northWest = mine.getNorthWest();


            if(north.hasTile() && northEast.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, north.topTile(), northEast.topTile()))
                        returnMe.add(new Placement(mine, north, northEast, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 1));
            }
            if(northEast.hasTile() && southEast.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, northEast.topTile(), southEast.topTile()))
                    returnMe.add(new Placement(mine, northEast, southEast, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 2 ));
            }
            if(southEast.hasTile() && south.topTile() != null) {
                if(canPlaceOnHexTiles(placeAt, southEast.topTile(), south.topTile()))
                    returnMe.add(new Placement(mine, southEast, south, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 3));
            }
            if(south.hasTile() && southWest.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, south.topTile(), southWest.topTile()))
                    returnMe.add(new Placement(mine, south, southWest, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 4));
            }
            if(southWest.hasTile() && northWest.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, southWest.topTile(), northWest.topTile()))
                    returnMe.add(new Placement(mine, southWest, northWest, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 5));
            }
            if(northWest.hasTile() && north.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, northWest.topTile(), north.topTile()))
                    returnMe.add(new Placement(mine, northWest, north, volcanoTile, clockwiseNonVolcanoTile, counterClockwiseNonVolcanoTile, 6));
            }
        } else { //only allow volcano tile placements on top of of other tiles
            return null;
        }

        return returnMe;
    }

    public boolean canPlaceOnHexTiles(HexTile ht1, HexTile ht2, HexTile ht3) {
        boolean areSameLevel = ht1.getLevel() == ht2.getLevel() && ht2.getLevel() == ht3.getLevel();
        boolean areNotInSameTriHexTile = !(ht1.getTriHexTile() == ht2.getTriHexTile() && ht2.getTriHexTile() == ht3.getTriHexTile());
        boolean areAdjacent = OffsetCoordinate.areAdjacent(ht1.getLocation(),ht2.getLocation()) && OffsetCoordinate.areAdjacent(ht2.getLocation(),ht3.getLocation())
                && OffsetCoordinate.areAdjacent(ht1.getLocation(),ht3.getLocation());
        boolean doNotContainTotorosOrTigers = !ht1.hasTotoro() && !ht1.hasTiger() && !ht2.hasTotoro() && !ht2.hasTiger() && !ht3.hasTotoro() && !ht3.hasTiger();
        boolean doNotContainEntireSettlements = !containEntireSettlements(ht2, ht3);
        return areSameLevel && areNotInSameTriHexTile && areAdjacent && doNotContainEntireSettlements && doNotContainTotorosOrTigers;
    }

    public boolean containEntireSettlements(HexTile ht1, HexTile ht2) {
        if (!ht1.isOccupied() && !ht2.isOccupied()) {
            return false;
        }
        else if (ht1.isOccupied() && !ht2.isOccupied()) {
            return containsSize1Settlement((TerrainTile) ht1);
        }
        else if (!ht1.isOccupied() && ht2.isOccupied()) {
            return containsSize1Settlement((TerrainTile) ht2);
        }
        else {
            if (ht1.getOwner() == ht2.getOwner())
                return containSize2Settlement((TerrainTile) ht1, (TerrainTile) ht2);
            else
                return containsSize1Settlement((TerrainTile) ht1) || containsSize1Settlement((TerrainTile) ht2);
        }
    }

    private boolean containsSize1Settlement(TerrainTile tt) {
        int numberOfFriendlyNeighbors = tt.getNumberOfFriendlyNeighbors();
        return numberOfFriendlyNeighbors == 0;
    }

    private boolean containSize2Settlement(TerrainTile tt1, TerrainTile tt2) {
        int numberOfTt1FriendlyNeighbors = tt1.getNumberOfFriendlyNeighbors();
        int numberOfTt2FriendlyNeighbors = tt2.getNumberOfFriendlyNeighbors();
        return numberOfTt1FriendlyNeighbors == 1 && numberOfTt2FriendlyNeighbors == 1;
    }

    /**
     * All legal placements > 0
     */
    public ArrayList<Placement> getLegalMapPlacements(TriHexTile tht) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        for (BoardSpace bs : gameBoard.values()) {
            if(bs.hasTile()) {
                HexTile thisTile = bs.topTile();
                if (thisTile.terrainType() == TerrainType.VOLCANO)
                    returnMe.addAll(getLegalPlacementsAtHexTile(tht, thisTile));

            }
        }
        return returnMe;
    }
    /* returns all triplets of adjacent tiles that aren't all in the same tri-hex tile
     * TODO this would be useful for ignoring illegal placements
     */

//    public List<HexTile> sameLevelAdjacentHexesOfDifferentTriHexes() {
//        List<HexTile> returnMe;
//        for
//    }

    public ArrayList<Placement> getAllLegalPlacements(TriHexTile tht) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        returnMe.addAll(getLegalMapPlacements(tht));
        returnMe.addAll(getLegalTablePlacements(tht));
        return returnMe;
    }

    /*
        takes a placement object and implements it's effects on the board
     */
    public void implementPlacement(Placement p) {
        p.place();
        for (BoardSpace b : p.getBoardSpaces()) {
            addRadialBoardSpaces(2, b);
            this.connectAdjacentBoardSpaces(b);
            this.activateAdjacentBoardSpaces(b);
        }
        this.playedTriHexTiles.add(p.getBoardSpaces().get(0).topTile().getTriHexTile());
        //p.getBoardSpaces().get(0).topTile().resetOwner();
    }

    private Placement tempPlacement;

    //same as implementPlacement except it doesn't activate board spaces, so it's easier to clean up
    //ALWAYS CALL revokeLastPlacement() AFTER USING THIS, then call implementPlacement if you want to
    public void temporaryPlacement(Placement p){
        tempPlacement = p;
        p.place();
    }

    //NOTE: BE VERY CAREFUL WITH THIS
    public void revokeLastPlacement() {
        tempPlacement.revokeTemporaryPlacement();
    }

    public boolean isLegalPlacement(Placement p) {
        if (p.isLevelPlacement() && p.isOverlapping() && !p.volcanoMatch())
            return true;
        else
            return false;
    }

    /*
        returns a list of all non-null, top-level hextiles on the board (i.e. tiles that have been placed)
     */
    public ArrayList<HexTile> getVisible() {
        ArrayList<HexTile> visible = new ArrayList<HexTile>();
        Collection<BoardSpace> bs = gameBoard.values();
        for (BoardSpace b : bs) {
            if (b.hasTile())
                visible.add(b.topTile());
        }
        return visible;
    }

    public HexTile getVisibleAtAxialCoordinate(OffsetCoordinate offsetCoordinate) {
        return gameBoard.get(offsetCoordinate).topTile();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
//        for (TriHexTile tht : this.playedTriHexTiles) {
//            HexTile curr;
//            curr = tht.getTileOne();
//            if (curr.getLevel() > 1) {
//                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
//                curr = tht.getTileTwo();
//                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
//                curr = tht.getTileThree();
//                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
//            }
//        }
        int level1 = 0;
        int level2 = 0;
        int level3 = 0;
        int level4 = 0;
        for(HexTile ht: getVisible()){
            switch (ht.getLevel()){
                case 0:
                    break;
                case 1:
                    level1++;
                    break;
                case 2:
                    level2++;
                    break;
                case 3:
                    level3++;
                    break;
                case 4:
                    level4++;
                    break;
            }
        }
        if(level1 %3 != 0){
            sb.append("LEVEL 1 UNEQUAL");
        }
        if(level2 %3 != 0){
            sb.append("LEVEL 2 UNEQUAL");
        }
        if(level3 %3 != 0){
            sb.append("LEVEL 3 UNEQUAL");
        }
        if(level4 %3 != 0){
            sb.append("LEVEL 4 UNEQUAL");
        }
        return sb.toString();
    }

    public int getNumberOfTriHextiles() {
        return numberOfTriHextiles;
    }

    /*
        given a hextile, do a BFS or DFS for all contiguos, connected tiles of the same type and return them as a list
     */
    public ContiguousTerrainTypeTiles getContiguousTerrainFromTile(HexTile ht) {
        return new ContiguousTerrainTypeTiles(ht);
    }

    public int getNumberOfBoardSpaces() {
        return gameBoard.size();
    }

    public void cleanup(){
        playedTriHexTiles = new ArrayList<>();
        numberOfTriHextiles =0;
        firstTurn = true;
        gameBoard = new HashMap<OffsetCoordinate,BoardSpace>();
    }
}


class Distance {
    private BoardSpace mine;
    private int distance = 0;

    public Distance(int distance, BoardSpace me) {
        this.distance = distance;
        mine = me;
    }

    public BoardSpace getBoardSpace() {
        return mine;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return mine.getLocation().hashCode();
    }
}

