package game;

import javax.swing.*;

public class Game {
    private GameBoard board;
    private GamePlayer[] gamePlayers = new GamePlayer[2];
    private int playersTurn = 0;

    public Game() { this.board = new GameBoard(this); }

    public void initGame() {
        gamePlayers[0] = new GamePlayer(true, 'X');
        gamePlayers[1] = new GamePlayer(false, 'O');
    }

    void passTurn() { playersTurn = (playersTurn == 0) ? 1 : 0; }

    GamePlayer getCurrentPlayer() { return gamePlayers[playersTurn]; }

    GamePlayer getNextPlayer() {
        if (playersTurn == 1) {
            return gamePlayers[0];
        }
        return gamePlayers[1];
    }

    void showMessage(String messageText){
        JOptionPane.showMessageDialog(board, messageText);
    }
}
