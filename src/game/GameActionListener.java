package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;

    public GameActionListener(int row, int cell, GameButton gButton) {
        this.row = row;
        this.cell = cell;
        this.button = gButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();
        if (board.getGame().getCurrentPlayer().getPlayerSign() == '0')
            board.getGame().passTurn();

        if (board.isTurnable(row, cell)) {
            updateByPlayerData(board);

            if (board.isFull()) {
                board.getGame().showMessage("Nobody Wins");
                board.emptyField();
                board.getGame().passTurn();
            }
            else {
                updateByAIData(board);
            }
        } else {
            board.getGame().showMessage("Wrong turn");
        }
    }

    private void updateByPlayerData(GameBoard board) {
        board.updateGameField(row, cell);

        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()) {
            button.getBoard().getGame().showMessage("You WIN");
            board.emptyField();

        }
        else {
            board.getGame().passTurn();
        }
    }

    private void updateByAIData(GameBoard board) {
        int xy[] = new int[2];

        board.findBestCell(xy);
        board.updateGameField(xy[0], xy[1]);
        int cellIndex = GameBoard.dimension * xy[0] + xy[1];
        board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()) {
            button.getBoard().getGame().showMessage("Computer WIN!");
            board.emptyField();
        }
        else if (board.isFull()) {
            board.getGame().showMessage("Nobody Wins");
            board.emptyField();
        }
        board.getGame().passTurn();
    }
}