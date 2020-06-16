package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    public static int dimension = 7;
    static int cellSize = 150;
    private char[][] gameField;
    private GameButton[] gameButtons;
    private Game game;
    static char nullSymbol = '\u0000';

    public GameBoard(Game currentGame) {
        this.game = currentGame;
        initField();
    }

    private void initField() {
        setBounds(400, 300, cellSize * dimension, cellSize * dimension);
        setLocationRelativeTo(null);
        setTitle("TikTakToe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { emptyField(); }
        });

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize *  dimension, 150);

        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension * dimension];

        for (int i = 0; i < (dimension * dimension); i++) {
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    void emptyField() {
        for (int i = 0; i < (dimension * dimension); i++) {
            gameButtons[i].setText("");

            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;

            gameField[x][y] = nullSymbol;
        }
        game.initGame();
    }

    Game getGame() { return game; }

    boolean isTurnable(int x, int y) {
        boolean result = false;

        if (gameField[y][x] == nullSymbol)
            result = true;
        return result;
    }

    void updateGameField(int x, int y) {
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkWin() {
        boolean result = false;

        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        if (checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol))
            result = true;
        return result;
    }

    boolean checkWinLines(char playerSymbol) {
        boolean cols, rows, result;

        result = false;
        for (int col = 0; col < dimension; col++) {
            cols = true;
            rows = true;

            for (int row = 0; row < dimension; row++) {
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }

            if (cols || rows) {
                result = true;
                break;
            }

            if (result)
                break;
        }

        return result;
    }

    boolean checkWinDiagonals(char playerSymbol) {
        boolean d1, d2, result;

        result = false;
        d1 = true;
        d2 = true;
        for (int i = 0; i < dimension; i++) {
            d1 &= (gameField[i][i] == playerSymbol);
            d2 &= (gameField[dimension - 1 - i][i] == playerSymbol);
        }

        return d1 || d2;
    }

    boolean isFull() {
        boolean result = true;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] == nullSymbol)
                    result = false;
            }
        }

        return result;
    }

    public void findBestCell(int[] xy) {
        int hottestVal = -1;
        int currentVal;

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (gameField[y][x] == nullSymbol) {
                    if ((currentVal = countHeat(y, x)) > hottestVal) {
                        hottestVal = currentVal;
                        xy[0] = x;
                        xy[1] = y;
                    }
                }
            }
        }
    }

    private int countHeat(int y, int x) {
        int numOfComputerTags;
        int numOfPlayerTags;
        int totalHeat = 0;

        /*
        Check line:
         */
        numOfComputerTags = 1;
        numOfPlayerTags = 0;
        for (int i = 0; i < dimension; i++) {
            numOfComputerTags += (gameField[y][i] == game.getCurrentPlayer().getPlayerSign()) ? 1 : 0;
            numOfPlayerTags += (gameField[y][i] == game.getNextPlayer().getPlayerSign()) ? 1 : 0;
        }
        totalHeat += analiseHeat(numOfComputerTags, numOfPlayerTags);

        /*
        Check column:
         */
        numOfComputerTags = 1;
        numOfPlayerTags = 0;
        for (int i = 0; i < dimension; i++) {
            numOfComputerTags += (gameField[i][x] == game.getCurrentPlayer().getPlayerSign()) ? 1 : 0;
            numOfPlayerTags += (gameField[i][x] == game.getNextPlayer().getPlayerSign()) ? 1 : 0;
        }
        totalHeat += analiseHeat(numOfComputerTags, numOfPlayerTags);

        /*
        Check 1st diagonal:
         */
        if (y == x) {
            numOfComputerTags = 1;
            numOfPlayerTags = 0;
            for (int i = 0; i < dimension; i++) {
                numOfComputerTags += (gameField[i][i] == game.getCurrentPlayer().getPlayerSign()) ? 1 : 0;
                numOfPlayerTags += (gameField[i][i] == game.getNextPlayer().getPlayerSign()) ? 1 : 0;
            }
            totalHeat += analiseHeat(numOfComputerTags, numOfPlayerTags);
        }

        /*
        Check 2nd diagonal:
         */
        if (y == (dimension - 1 - x)) {
            numOfComputerTags = 1;
            numOfPlayerTags = 0;
            for (int i = 0; i < dimension; i++) {
                numOfComputerTags += (gameField[i][dimension - 1 - i] == game.getCurrentPlayer().getPlayerSign()) ? 1 : 0;
                numOfPlayerTags += (gameField[i][dimension - 1 - i] == game.getNextPlayer().getPlayerSign()) ? 1 : 0;
            }
            totalHeat += analiseHeat(numOfComputerTags, numOfPlayerTags);
        }
        return totalHeat;
    }

    private static int analiseHeat(int numOfComputerTags, int numOfPlayerTags) {
        if (numOfComputerTags == dimension && numOfPlayerTags == 0) {
            return 1000;
        } else if (numOfPlayerTags == dimension - 1) {
            return 500;
        } else if (numOfPlayerTags > 0) {
            return 0;
        } else {
            return numOfComputerTags;
        }
    }

    public GameButton[] getGameButtons() { return gameButtons; }

    public GameButton getButton(int buttonIndex) { return gameButtons[buttonIndex]; }

    public void printMap() {
        int     i = 0;
        for (;i <= dimension; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        i = 1;
        for (char[] line : gameField) {
            System.out.print(i++ + "  ");
            for (char sqr : line) {
                System.out.print(sqr + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}