package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jowens on 3/8/17.
 */
public class GameMap {

    //todo decide between these two
    private BoardSpace[][] gameBoard;
    private HashMap<AxialCoordinate, BoardSpace> gameBoard2;
    private int numberOfTriHextiles;

    private static int boardLength = 20, boardWidth = 20;

    private List<BoardSpace> activeSpaces;

    public GameMap() {
        initializeBoard();
    }

    //using axial coordinate system, initialize the board to prepare it for play
    public void initializeBoard() {

        //gameBoard = new BoardSpace[boardWidth][boardLength]; //TODO minimize this size to the max possible number of tiles
        gameBoard2 = new HashMap<AxialCoordinate, BoardSpace>();

        /*
        //assign each board space its adjacent boardspaces OLD WAY
        for(int x=0;x!=boardWidth; ++x){
            for(int y=0;y!=boardLength; ++y){
                AxialCoordinate current = new AxialCoordinate(x,y);
                ArrayList bsAdjacentBoardSpaces = new ArrayList<BoardSpace>();

                //populating adjacencies for each of the board-spaces
                gameBoard[x][y].setNorth(gameBoard[current.getNorth().x][current.getNorth().y]);
                gameBoard[x][y].setNorthEast(gameBoard[current.getNorthEast().x][current.getNorthEast().y]);
                gameBoard[x][y].setNorthWest(gameBoard[current.getNorthWest().x][current.getNorthWest().y]);
                gameBoard[x][y].setSouth(gameBoard[current.getSouth().x][current.getSouth().y]);
                gameBoard[x][y].setSouthEast(gameBoard[current.getSouthEast().x][current.getSouthEast().y]);
                gameBoard[x][y].setSouthWest(gameBoard[current.getSouthWest().x][current.getSouthWest().y]);
            }
        }
        gameBoard[boardWidth/2][boardLength/2].setActive(); //first tile is in the middle of the board to maximize distance to edge
        //gameBoard2.forEach();
        */

        //adding first board space
        BoardSpace first = new BoardSpace(new ArrayList<BoardSpace>());
        first.setLocation(new AxialCoordinate(0,0));
        gameBoard2.put(first.getLocation(), first);
        numberOfTriHextiles = 0;
    }

    public void placeTriHexTile(BoardSpace whereAWillGo, Direction whereWillBGo, TriHexTile toPlace){
        //whereAWillGo.addTile();
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
//        gameBoard[boardWidth / 2][boardLength / 2].addTile(first.getTileOne()); //origin
//        gameBoard[boardWidth / 2][boardLength / 2 - 1].addTile(first.getTileTwo()); //northwest TODO allow user to rotate initial input
//        gameBoard[boardWidth / 2 + 1][boardLength / 2 - 1].addTile(first.getTileThree()); //northeast
        BoardSpace firstBS = gameBoard2.get(new AxialCoordinate(0,0));;
        addAdjacentBoardSpaces(firstBS.getLocation());

        gameBoard2.get(firstBS.getLocation()).addTile(first.a);
        gameBoard2.get(firstBS.getNorth().getLocation()).addTile(first.b);
        addAdjacentBoardSpaces(firstBS.getNorth().getLocation());
        gameBoard2.get(firstBS.getNorthEast().getLocation()).addTile(first.c);
        addAdjacentBoardSpaces(firstBS.getNorthEast().getLocation());

        numberOfTriHextiles++;
    }

    //TODO this currently doesn't work
    public List<Placement> getLegalPlacements(TriHexTile tht) {
        List<Placement> returnMe = new ArrayList<Placement>();
        HexTile ht1,ht2,ht3;

        ht1 = tht.getTileOne();
        ht2 = tht.getTileTwo();
        ht3 = tht.getTileThree();

        for(BoardSpace bs: activeSpaces){ //for each active board space (i.e. above a played tile, or adjacent to one)
            if(bs.isEmpty()){
                //get all placements that are possible given adjacent empty boardspaces
                if(bs.getNorth().isEmpty()) {
                    Placement p = new Placement(bs, bs.getNorth(), bs.getNorthEast(), ht1, ht2, ht3);
                }
            }
        }
        return returnMe;
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

        //ht1
        if(placeAt.ofSameType(ht1)){ // hex-tiles can't be placed on tiles of different type
            if(placeAt.getNorth().ofSameType(ht2)){
                if(placeAt.getNorthEast().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorth().getBoardSpace(),placeAt.getNorthEast().getBoardSpace(), ht1, ht2, ht3);

                }
            }
            if(placeAt.getNorthEast().ofSameType(ht2)){
                if(placeAt.getSouthEast().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthEast().getBoardSpace(),placeAt.getSouthEast().getBoardSpace(), ht1, ht2, ht3);

                }
            }
            if(placeAt.getSouthEast().ofSameType(ht2)){
                if(placeAt.getSouth().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthEast().getBoardSpace(),placeAt.getSouth().getBoardSpace(), ht1, ht2, ht3);

                }
            }
            if(placeAt.getSouth().ofSameType(ht2)){
                if(placeAt.getSouthWest().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouth().getBoardSpace(),placeAt.getSouthWest().getBoardSpace(), ht1, ht2, ht3);

                }
            }
            if(placeAt.getSouthWest().ofSameType(ht2)){
                if(placeAt.getNorthWest().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthWest().getBoardSpace(),placeAt.getNorthWest().getBoardSpace(), ht1, ht2, ht3);

                }
            }
            if(placeAt.getNorthWest().ofSameType(ht2)){
                if(placeAt.getNorth().ofSameType(ht3)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthWest().getBoardSpace(),placeAt.getNorth().getBoardSpace(), ht1, ht2, ht3);

                }
            }
        }
        if(placeAt.ofSameType(ht2)){
            if(placeAt.getNorth().ofSameType(ht3)){
                if(placeAt.getNorthEast().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorth().getBoardSpace(),placeAt.getNorthEast().getBoardSpace(), ht2, ht3, ht1);

                }
            }
            if(placeAt.getNorthEast().ofSameType(ht3)){
                if(placeAt.getSouthEast().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthEast().getBoardSpace(),placeAt.getSouthEast().getBoardSpace(), ht2, ht3, ht1);

                }
            }
            if(placeAt.getSouthEast().ofSameType(ht3)){
                if(placeAt.getSouth().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthEast().getBoardSpace(),placeAt.getSouth().getBoardSpace(), ht2, ht3, ht1);

                }
            }
            if(placeAt.getSouth().ofSameType(ht3)){
                if(placeAt.getSouthWest().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouth().getBoardSpace(),placeAt.getSouthWest().getBoardSpace(), ht2, ht3, ht1);

                }
            }
            if(placeAt.getSouthWest().ofSameType(ht3)){
                if(placeAt.getNorthWest().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthWest().getBoardSpace(),placeAt.getNorthWest().getBoardSpace(), ht2, ht3, ht1);

                }
            }
            if(placeAt.getNorthWest().ofSameType(ht3)){
                if(placeAt.getNorth().ofSameType(ht1)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthWest().getBoardSpace(),placeAt.getNorth().getBoardSpace(), ht2, ht3, ht1);

                }
            }

        }
        if(placeAt.ofSameType(ht3)){
            if(placeAt.getNorth().ofSameType(ht1)){
                if(placeAt.getNorthEast().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorth().getBoardSpace(),placeAt.getNorthEast().getBoardSpace(), ht3,ht1,ht2);

                }
            }
            if(placeAt.getNorthEast().ofSameType(ht1)){
                if(placeAt.getSouthEast().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthEast().getBoardSpace(),placeAt.getSouthEast().getBoardSpace(), ht3,ht1,ht2);

                }
            }
            if(placeAt.getSouthEast().ofSameType(ht1)){
                if(placeAt.getSouth().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthEast().getBoardSpace(),placeAt.getSouth().getBoardSpace(), ht3,ht1,ht2);

                }
            }
            if(placeAt.getSouth().ofSameType(ht1)){
                if(placeAt.getSouthWest().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouth().getBoardSpace(),placeAt.getSouthWest().getBoardSpace(), ht3,ht1,ht2);

                }
            }
            if(placeAt.getSouthWest().ofSameType(ht1)){
                if(placeAt.getNorthWest().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getSouthWest().getBoardSpace(),placeAt.getNorthWest().getBoardSpace(), ht3,ht1,ht2);

                }
            }
            if(placeAt.getNorthWest().ofSameType(ht1)){
                if(placeAt.getNorth().ofSameType(ht2)){
                    //law of demeter violation
                    Placement p = new Placement(placeAt.getBoardSpace(), placeAt.getNorthWest().getBoardSpace(),placeAt.getNorth().getBoardSpace(), ht3,ht1,ht2);

                }
            }
        }
        return null;
    }

    /*
     * returns all triplets of adjacent tiles that aren't all in the same tri-hex tile
     */
//    public List<TriHexTile> sameLevelAdjacentHexesOfDifferentTriHexes() {
//        List<HexTile> returnMe;
//        for
//    }


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
    public List<HexTile> getGameBoard() {
        ArrayList<HexTile> list = new ArrayList<HexTile>();
        for (BoardSpace[] ht : gameBoard) {
            for (BoardSpace ht2 : ht) {
                if (!ht2.isEmpty()) {
                    list.add(ht2.topTile());
                }
            }
        }
        return list;
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
}
