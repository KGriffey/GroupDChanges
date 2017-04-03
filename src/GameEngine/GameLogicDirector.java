package GameEngine;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.PlayerController;
import GameControl.Player.WhitePlayer;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.Deck;

import java.util.ArrayList;


/**
 * Created by jowens on 3/8/17.
 * responsible for running the game temporally
 */
public class GameLogicDirector implements Runnable{
    private static GameLogicDirector me;

    private boolean newGame = true;


    private boolean AIgame = true;
    private boolean AIvsHuman = false;
    private boolean HumanVsHuman = false;

    //Game specific objects
    Player p1,p2;

    Player winner;
    PlayerController activePlayer;
    Player currentPlayer;

    ArrayList<Player> players;
    public Deck deck;
    GameController gc;
    private GameMap myMap;

    public GameLogicDirector(){
        myMap = new GameMap();
        deck = new Deck();
        winner=null;
    }


    public static GameLogicDirector getInstance(){
        if(me == null)
            me= new GameLogicDirector();
        return me;
    }

    public void begin(){
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    private void nextPlayer(){
        if(currentPlayer == p1)
            currentPlayer =p2;
        else
            currentPlayer =p1;
    }
    /*
      NEVER CALL THIS - DAVE
     */
    public void run() {
        while (winner == null) {
            if (newGame) {
                System.out.println("Initializing new game.");
                initializeNewGame();
                winner = null;
                gc = GameController.getInstance();
                gc.initViewControllerInteractions(p1, activePlayer);
            } else {
                //game logic
                System.out.println("cards left" + deck.cardsLeft());
                if (deck.cardsLeft() > 0) {
                    if (AIgame) {
                        AIvsAIGameTurn();
                    } else if (AIvsHuman) {
                        AIvsHumanGameTurn();
                    }
                    gc.paint();

                } else { //game over
                    System.out.println();
                    System.out.println(myMap);
                    for (Player p : players) {
                        System.out.println("Round " + (48 - deck.cardsLeft()));
                        System.out.println("cards left" + deck.cardsLeft());
                        System.out.println(p.toString() + " Score: " + p.getScore());
                        p.takeTurn(myMap, deck.draw());

                        //check if the player has only one type of tokens left. If yes, end the game.
                        if (p.checkOnlyOneTypeTokenIsLeft()) {
                            winner = p;
                            break;
                        } else if (deck.cardsLeft() == 0) {
                            break;
                        }

                        System.out.println("\n");
                        gc.paint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            System.out.println(ie.getStackTrace());
                        }
                    }
                    //check if the deck is out of unplayed tiles. If yes, run game winner check and end the game.
                    if (deck.cardsLeft() == 0) {
                        winner = gameEndCheckWinner();
                        gc.paint();
                    }

                    if (deck.cardsLeft() % 10 == 0) {
                        gc.paint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            System.out.println(ie.getStackTrace());
                        }
                    }

                }
            }
            System.out.println("Winner: " + winner.toString());
        }
    }

    public Player gameEndCheckWinner(){
        Player winner = compareScores();
        if(winner==null){
            winner = compareTotoroCount();
            if(winner==null){
                winner = compareTigerCount();
                if(winner==null){
                    winner = compareMeepleCount();
                }
            }
        }
        return winner;
    }

    private Player compareScores(){
        Player winner;
        if(p1.getScore() > p2.getScore()){
            winner = p1;
        } else if(p1.getScore() < p2.getScore()){
            winner = p2;
        } else
            winner = null; //score ties
        return winner;
    }

    private void AIvsHumanGameTurn(){}


    public void AIvsAIGameTurn(){

        System.out.println("Round " + (48 - deck.cardsLeft()));
        System.out.println("Score " + currentPlayer.getScore());

        for(Player p: players){
            AItakeTurn();
            nextPlayer();
        }
    }

    public void AItakeTurn() {
        currentPlayer.takeTurn(myMap, deck.draw());
        System.out.println(getMap());
        System.out.println("\n");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            System.out.println(ie.getStackTrace());
        }
    }
    private Player compareTotoroCount(){
        Player winner;
        if(p1.getTotoroCount() < p2.getTotoroCount()){
            winner = p1;
        } else if(p1.getTotoroCount() > p2.getTotoroCount()){
            winner = p2;
        } else
            winner = null; //totoro count ties
        return winner;
    }

    private Player compareTigerCount(){
        Player winner;
        if(p1.getTigerCount() < p2.getTigerCount()){
            winner = p1;
        } else if(p1.getTigerCount() > p2.getTigerCount()){
            winner = p2;
        } else
            winner = null; //tiger count ties
        return winner;
    }

    private Player compareMeepleCount(){
        Player winner;
        if(p1.getMeepleCount() < p2.getMeepleCount()){
            winner = p1;
        } else {
            winner = p2;
        }
        return winner;
    }

    public GameMap getMap(){
        return myMap;
    }

    private void initializeNewGame() {
        p1 = new WhitePlayer();
        p2 = new BlackPlayer();

        players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        activePlayer = new PlayerController(p1);
        currentPlayer = p1;

        GameController gameController = new GameController();
        gameController.initViewControllerInteractions(p1, activePlayer);
        deck = Deck.newExampleDeck();
        System.out.println(deck.cardsLeft());

        newGame = false; // Q: what's this for? A: see run method
        gc = new GameController();

    }

}
