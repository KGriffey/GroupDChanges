package GameControl.Player;

import GameModel.Map.GameMap;

/**
 * Created by jowens on 3/10/17.
 */
public class WhitePlayer extends Player {

//    public WhitePlayer(){
//        this("WhitePlayer", null);
//    }

    public WhitePlayer(String name, GameMap gameMap, Player enemy){
        super(gameMap, enemy);
        this.name = name;
    }

    public boolean isWhite(){
        return true;
    }

}
