package com.university.checkers.checkers;

import com.university.checkers.Utils;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int BOARD_SIZE = 8;
    public static final int CHECKERS_AMOUNT = 12;

    private final Cell[][] board;
    private boolean playerSide;               //false - white; true - black.
    private final String playerSideStr;
    private final String computerSideStr;
    private int whiteCheckersAmount;
    private int blackCheckersAmount;


    public Board() {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        whiteCheckersAmount = CHECKERS_AMOUNT;
        blackCheckersAmount = CHECKERS_AMOUNT;
        playerSide = false;
        playerSideStr = Utils.convertBooleanToSide(playerSide);
        computerSideStr = Utils.convertBooleanToSide(!playerSide);
    }

    public Board(boolean side) {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        whiteCheckersAmount = CHECKERS_AMOUNT;
        blackCheckersAmount = CHECKERS_AMOUNT;
        playerSide = side;
        playerSideStr = Utils.convertBooleanToSide(playerSide);
        computerSideStr = Utils.convertBooleanToSide(!playerSide);
    }


    //	     0   _  2   _  4   _  6   _
    //	     _   9  _  11  _  13  _  15
    //	     16  _  18  _  20  _  22 _
    //	     _  25  _  27  _  29  _  31
    //	     32  _  34  _  36  _  38  _
    //	     _  41  _  43  _  45  _  47
    //	     48  _  50  _  52  _  54
    //	     _  57  _  59  _  61  _  63

    public void initializeBoard() {

        int cellNumberCounter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Cell(i, j, cellNumberCounter);
                cellNumberCounter++;
            }
        }


        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if ((i < 3) && (i + j) % 2 == 1) {
                    board[i][j].setCurrentChecker(playerSide ? new Checker(Checker.WHITE, board[i][j]) : new Checker(Checker.BLACK, board[i][j]));
                    board[BOARD_SIZE - i - 1][BOARD_SIZE - j - 1].setCurrentChecker(playerSide ? new Checker(Checker.BLACK, board[BOARD_SIZE - i - 1][BOARD_SIZE - j - 1]) : new Checker(Checker.WHITE, board[BOARD_SIZE - i - 1][BOARD_SIZE - j - 1]));
                }
            }
        }
    }

    public void setSide(boolean playerSide) {
        this.playerSide = playerSide;
    }

    public void printBoard() {

        StringBuilder temp;
        for (int i = 0; i < BOARD_SIZE; i++) {
            temp = new StringBuilder();
            for (int j = 0; j < BOARD_SIZE; j++) {
                temp.append(board[i][j]);
                temp.append(" ");
            }
            System.out.print(temp.toString());
            System.out.println();
        }
        System.out.println();
    }

    public Cell getCell(int x, int y) {

        if (Utils.isCordsCorrect(x, y)) {
            return board[x][y];
        }

        return null;
    }

    public Checker obtainChecker(int x, int y) {

        if (Utils.isCordsCorrect(x, y)) {
            return board[x][y].getCurrentChecker();
        }

        return null;
    }

    public int getWhiteCheckersAmount() {
        return whiteCheckersAmount;
    }

    public int getBlackCheckersAmount() {
        return blackCheckersAmount;
    }

    public boolean isPlayerSide() {
        return playerSide;
    }

    public String getPlayerSideStr() {
        return playerSideStr;
    }

    public String getComputerSideStr() {
        return computerSideStr;
    }

    private boolean isCellContainsChecker(int x, int y) {
        if (Utils.isCordsCorrect(x, y)) {
            return board[x][y].containsChecker();
        }

        return false;
    }

    public void removeCheckerFromGame(int x, int y) {
        if (isCellContainsChecker(x, y)) {
            if (board[x][y].getCurrentChecker().getSide().equals(Checker.WHITE)) {
                whiteCheckersAmount--;
            } else {
                blackCheckersAmount--;
            }
            board[x][y].removeChecker();
        }
    }

    public void removeCheckerFromGame(Cell sourceCell) {
        if (sourceCell.containsChecker()) {
            if (sourceCell.getCurrentChecker().getSide().equals(Checker.WHITE)) {
                whiteCheckersAmount--;
            } else {
                blackCheckersAmount--;
            }
            sourceCell.removeChecker();
        }
    }

    public boolean checkIfCheckerIsOnQueenCell(Cell sourceCell) {

        if (sourceCell == null) {
            return false;
        }

        Checker sourceChecker = sourceCell.getCurrentChecker();

        if (sourceChecker == null) {
            return false;
        }

        if (sourceCell.getxCord() != 0 && sourceCell.getxCord() != 7) {
            return false;
        }

        int xCordBlackQueen;
        int xCordWhiteQueen;
        if (this.playerSideStr.equals(Checker.WHITE)) {
            xCordBlackQueen = 7;
            xCordWhiteQueen = 0;
        } else {
            xCordBlackQueen = 0;
            xCordWhiteQueen = 7;
        }

        if (sourceChecker.getSide().equals(Checker.WHITE) && sourceCell.getxCord() == xCordWhiteQueen) {
            return true;
        }

        if (sourceChecker.getSide().equals(Checker.BLACK) && sourceCell.getxCord() == xCordBlackQueen) {
            return true;
        }

        return false;
    }

    public void makeCheckerQueen(Cell sourceCell) {

        if (sourceCell == null) {
            return;
        }

        Checker sourceChecker = sourceCell.getCurrentChecker();

        if (sourceChecker == null) {
            return;
        }

        sourceChecker.setQueen(true);
    }

    /**
     * returns whether the checkers has been beaten
     */
    public boolean doMove(Cell sourceCell, Cell destCell, boolean isComputerSide) throws Exception {

        if (sourceCell == null || destCell == null) {
            throw new Exception("SourceCell == 0 || destCell == 0.[board.doMove() ERROR");
        }

        boolean result = false;
        Checker checkerToMove = sourceCell.getCurrentChecker();

        Cell deadEnemyCell = Utils.getDiagonalCellToAttack(sourceCell, destCell, this);
        if (deadEnemyCell != null) {
            removeCheckerFromGame(deadEnemyCell);
            result = true;
        }

        destCell.setCurrentChecker(checkerToMove);
        checkerToMove.setCoordinates(destCell);
        sourceCell.removeChecker();

        if (checkIfCheckerIsOnQueenCell(destCell)) {
            makeCheckerQueen(destCell);
        }

        return result;
    }

    public List<Checker> obtainAllComputerSideCheckers() {

        List<Checker> checkers = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].containsChecker()) {
                    Checker temp = board[i][j].getCurrentChecker();
                    if (temp.getSide().equals(this.computerSideStr)) {
                        checkers.add(temp);
                    }
                }
            }
        }
        return checkers;
    }
}