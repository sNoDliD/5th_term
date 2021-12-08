package com.university.checkers;

import com.university.checkers.checkers.Board;
import com.university.checkers.checkers.Cell;
import com.university.checkers.checkers.Checker;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isCordsCorrect(int xCord, int yCord) {
        return xCord >= 0 && xCord <= Board.BOARD_SIZE - 1 && yCord >= 0 && yCord <= Board.BOARD_SIZE - 1;
    }

    public static int convertIntToDirection(int input) {
        if (input >= 1) {
            return 1;
        }
        if (input <= -1) {
            return -1;
        }
        return 0;
    }

    public static Cell getDirection(Cell attacker, Cell victim) {
        int xCordDif = victim.getxCord() - attacker.getxCord();
        int yCordDif = victim.getyCord() - attacker.getyCord();

        xCordDif = convertIntToDirection(xCordDif);
        yCordDif = convertIntToDirection(yCordDif);

        return new Cell(xCordDif, yCordDif, 100);
    }

    public static Cell getDirection(int xCord, int yCord) {
        xCord = convertIntToDirection(xCord);
        yCord = convertIntToDirection(yCord);

        return new Cell(xCord, yCord, 101);
    }

    public static String convertBooleanToSide(boolean side) {
        if (!side) {
            return Checker.WHITE;
        }
        return Checker.BLACK;
    }

    public static boolean convertStringToBoolean(String side) {
        return !side.equals(Checker.WHITE);
    }

    public static Cell getDiagonalCellToAttack(Cell sourceCell, Cell destCell, Board board) {

        int xSource = sourceCell.getxCord();
        int ySource = sourceCell.getyCord();
        int xDest = destCell.getxCord();
        int yDest = destCell.getyCord();

        if (xSource > xDest && ySource > yDest) {
            xSource--;
            ySource--;
            while (xSource > xDest && ySource > yDest) {
                Cell tempCell = board.getCell(xSource, ySource);
                if (tempCell.containsChecker()) {
                    return tempCell;
                }
                xSource--;
                ySource--;
            }

            return null;
        }

        if (xSource > xDest && ySource < yDest) {
            xSource--;
            ySource++;
            while (xSource > xDest && ySource < yDest) {
                Cell tempCell = board.getCell(xSource, ySource);
                if (tempCell.containsChecker()) {
                    return tempCell;
                }
                xSource--;
                ySource++;
            }

            return null;
        }

        if (xSource < xDest && ySource > yDest) {
            xSource++;
            ySource--;
            while (xSource < xDest && ySource > yDest) {
                Cell tempCell = board.getCell(xSource, ySource);
                if (tempCell.containsChecker()) {
                    return tempCell;
                }
                xSource++;
                ySource--;
            }

            return null;
        }

        if (xSource < xDest && ySource < yDest) {
            xSource++;
            ySource++;
            while (xSource < xDest && ySource < yDest) {
                Cell tempCell = board.getCell(xSource, ySource);
                if (tempCell.containsChecker()) {
                    return tempCell;
                }
                xSource++;
                ySource++;
            }

            return null;
        }

        return null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean areCellsAreNeighbours(Cell sourceCell, Cell destCell) {

        if (sourceCell == null || destCell == null) {
            return false;
        }

        int xSourceCell = sourceCell.getxCord();
        int ySourceCell = sourceCell.getyCord();
        int xDestCell = destCell.getxCord();
        int yDestCell = destCell.getyCord();

        return Math.abs(xSourceCell - xDestCell) == 1 && Math.abs(ySourceCell - yDestCell) == 1;
    }

    public static Cell findRearCellAttack(Cell attacker, Cell victim, Board board) {
        if (attacker == null || victim == null) {
            return null;
        }

        Checker chAttacker = attacker.getCurrentChecker();
        Checker chVictim = victim.getCurrentChecker();

        if (chVictim == null || chAttacker == null) {
            return null;
        }

        if (chAttacker.getSide().equals(chVictim.getSide())) {
            return null;
        }

        int xCordDif = attacker.getxCord() - victim.getxCord();
        int yCordDif = attacker.getyCord() - victim.getyCord();

        int xRearVictim = victim.getxCord() - xCordDif;
        int yRearVictim = victim.getyCord() - yCordDif;

        if (isCordsCorrect(xRearVictim, yRearVictim)) {
            return board.getCell(xRearVictim, yRearVictim);
        }

        return null;
    }

    public static Cell findRearCell(Cell source, Cell dest, Board board) {
        if (source == null || dest == null) {
            return null;
        }

        int xCordDif = source.getxCord() - dest.getxCord();
        int yCordDif = source.getyCord() - dest.getyCord();

        int xRearVictim = dest.getxCord() - xCordDif;
        int yRearVictim = dest.getyCord() - yCordDif;

        if (isCordsCorrect(xRearVictim, yRearVictim)) {
            return board.getCell(xRearVictim, yRearVictim);
        }

        return null;
    }

    public static boolean isCellCanAttackAnotherCell(Cell attacker, Cell victim, Board board) {

        if (attacker == null || victim == null) {
            return false;
        }

        if (attacker.getCurrentChecker() == null || victim.getCurrentChecker() == null) {
            return false;
        }

        if (attacker.getCurrentChecker().getSide().equals(victim.getCurrentChecker().getSide())) {
            return false;
        }
        Cell rearCell = findRearCellAttack(attacker, victim, board);

        if (rearCell != null) {
            return !rearCell.containsChecker();
        } else {
            return false;
        }
    }

    private static Cell isQueenCellHasEnemyToAttackDiagonal(Cell attacker, Cell direction, Board board, String side) {

        if (attacker == null || direction == null) {
            return null;
        }

        Checker currQueen = attacker.getCurrentChecker();


        Cell temp;
        while (direction != null && !direction.containsChecker()) {
            temp = direction;
            direction = findRearCell(attacker, direction, board);
            attacker = temp;
        }

        if (direction == null) {
            return null;
        }

        if (direction.getCurrentChecker() == null) {
            return null;
        }

        if (direction.getCurrentChecker().getSide().equals(side)) {
            return null;
        } else {
            temp = findRearCell(attacker, direction, board);

            if (temp != null && !temp.containsChecker()) {
                return temp;
            }
        }

        return null;
    }

    public static boolean isQueenCellHasEnemyToAttack(Cell attacker, Board board) {
        return getFirstCellsAfterQueenAttack(attacker, board) != null;
    }

    public static List<Cell> getFirstCellsAfterQueenAttack(Cell attacker, Board board) {
        if (attacker == null) {
            return null;
        }

        Cell rightUp = board.getCell(attacker.getxCord() - 1, attacker.getyCord() + 1);
        Cell rightDown = board.getCell(attacker.getxCord() + 1, attacker.getyCord() + 1);
        Cell leftUp = board.getCell(attacker.getxCord() - 1, attacker.getyCord() - 1);
        Cell leftDown = board.getCell(attacker.getxCord() + 1, attacker.getyCord() - 1);

        Cell diagonal1;
        Cell diagonal2;
        Cell diagonal3;
        Cell diagonal4;

        diagonal1 = isQueenCellHasEnemyToAttackDiagonal(attacker, rightUp, board, attacker.getCurrentChecker().getSide());
        diagonal2 = isQueenCellHasEnemyToAttackDiagonal(attacker, rightDown, board, attacker.getCurrentChecker().getSide());
        diagonal3 = isQueenCellHasEnemyToAttackDiagonal(attacker, leftUp, board, attacker.getCurrentChecker().getSide());
        diagonal4 = isQueenCellHasEnemyToAttackDiagonal(attacker, leftDown, board, attacker.getCurrentChecker().getSide());

        if (diagonal1 == null && diagonal2 == null && diagonal3 == null && diagonal4 == null) {
            return null;
        }

        List<Cell> result = new ArrayList<>();
        if (diagonal1 != null) {
            result.add(diagonal1);
        }
        if (diagonal2 != null) {
            result.add(diagonal2);
        }
        if (diagonal3 != null) {
            result.add(diagonal3);
        }
        if (diagonal4 != null) {
            result.add(diagonal4);
        }
        return result;
    }

    private static List<Cell> getAllPossibleMovesDiagonal(Cell attacker, Cell direction, Board board) {
        if (attacker == null || direction == null) {
            return null;
        }

        List<Cell> result = new ArrayList<>();
        Cell temp;
        while (direction != null && !direction.containsChecker()) {
            temp = direction;
            direction = findRearCell(attacker, direction, board);
            attacker = temp;
            result.add(attacker);
        }

        return result;
    }

    public static List<Cell> getAllPossibleMovesQueenNoAttack(Cell attacker, Board board) {

        if (attacker == null || !attacker.containsChecker() || !attacker.getCurrentChecker().isQueen()) {
            return null;
        }

        Cell rightUp = board.getCell(attacker.getxCord() - 1, attacker.getyCord() + 1);
        Cell rightDown = board.getCell(attacker.getxCord() + 1, attacker.getyCord() + 1);
        Cell leftUp = board.getCell(attacker.getxCord() - 1, attacker.getyCord() - 1);
        Cell leftDown = board.getCell(attacker.getxCord() + 1, attacker.getyCord() - 1);

        List<Cell> resultDiagonal1 = getAllPossibleMovesDiagonal(attacker, rightUp, board);
        List<Cell> resultDiagonal2 = getAllPossibleMovesDiagonal(attacker, rightDown, board);
        List<Cell> resultDiagonal3 = getAllPossibleMovesDiagonal(attacker, leftUp, board);
        List<Cell> resultDiagonal4 = getAllPossibleMovesDiagonal(attacker, leftDown, board);

        List<Cell> result = new ArrayList<>();

        if (resultDiagonal1 != null && resultDiagonal1.size() > 0) {
            result.addAll(resultDiagonal1);
        }
        if (resultDiagonal2 != null && resultDiagonal2.size() > 0) {
            result.addAll(resultDiagonal2);
        }
        if (resultDiagonal3 != null && resultDiagonal3.size() > 0) {
            result.addAll(resultDiagonal3);
        }
        if (resultDiagonal4 != null && resultDiagonal4.size() > 0) {
            result.addAll(resultDiagonal4);
        }
        return result;
    }

    public static List<Cell> getAllPossibleMovesQueenAttack(Cell attacker, Board board) {

        List<Cell> firstCellsAfterAttack = getFirstCellsAfterQueenAttack(attacker, board);

        List<Cell> result = new ArrayList<>();

        for (Cell currCell : firstCellsAfterAttack) {
            Cell direction = getDirection(attacker, currCell);
            Cell rearCell = board.getCell(currCell.getxCord() - direction.getxCord(), currCell.getyCord() - direction.getyCord());

            if (rearCell == null) {
                System.out.println("getAllPossibleMovesQueenAttack FUNCTION. NO REAR CELL");
                return null;
            }

            List<Cell> temp = getAllPossibleMovesDiagonal(rearCell, currCell, board);
            if (temp != null && temp.size() > 0) {
                result.addAll(temp);
            }
        }

        List<Cell> realResult = new ArrayList<>();

        for (Cell currCell : result) {
            int xCordDirDiagLeft = currCell.getxCord() * -1;
            int yCordDirDiagLeft = currCell.getyCord();
            Cell dirDiagLeft = getDirection(xCordDirDiagLeft, yCordDirDiagLeft);
            int xCordDirDiagRight = currCell.getxCord();
            int yCordDirDiagRight = currCell.getyCord() * -1;
            Cell dirDiagRight = getDirection(xCordDirDiagRight, yCordDirDiagRight);

            Cell nextCellLeft = board.getCell(currCell.getxCord() + dirDiagLeft.getxCord(), currCell.getyCord() + dirDiagLeft.getyCord());
            Cell nextCellRight = board.getCell(currCell.getxCord() + dirDiagRight.getxCord(), currCell.getyCord() + dirDiagRight.getyCord());

            if (isQueenCellHasEnemyToAttackDiagonal(currCell, nextCellLeft, board, attacker.getCurrentChecker().getSide()) != null || isQueenCellHasEnemyToAttackDiagonal(currCell, nextCellRight, board, attacker.getCurrentChecker().getSide()) != null) {
                realResult.add(currCell);
            }
        }

        if (realResult.size() > 0) {
            return realResult;
        } else {
            return result;
        }

    }

    public static boolean isCellHasEnemyToAttack(Cell currentCell, Board board) {

        if (currentCell == null) {
            return false;
        }

        Checker currChecker = currentCell.getCurrentChecker();
        if (currChecker == null) {
            return false;
        }

        int xCord = currentCell.getxCord();
        int yCord = currentCell.getyCord();

        //if the players turn

        if (currChecker.isQueen()) {
            return isQueenCellHasEnemyToAttack(currentCell, board);
        } else {
            int xCordRightUp = xCord - 1;
            int yCordRightUp = yCord + 1;
            int xCordLeftUp = xCord - 1;
            int yCordLeftUp = yCord - 1;
            int xCordRightDown = xCord + 1;
            int yCordRightDown = yCord + 1;
            int xCordLeftDown = xCord + 1;
            int yCordLeftDown = yCord - 1;

            Cell leftUpCell = board.getCell(xCordLeftUp, yCordLeftUp);
            Cell rightUpCell = board.getCell(xCordRightUp, yCordRightUp);
            Cell leftDownCell = board.getCell(xCordLeftDown, yCordLeftDown);
            Cell rightDownCell = board.getCell(xCordRightDown, yCordRightDown);

            if (isCellCanAttackAnotherCell(currentCell, leftUpCell, board)) {
                return true;
            }
            if (isCellCanAttackAnotherCell(currentCell, rightUpCell, board)) {
                return true;
            }
            if (isCellCanAttackAnotherCell(currentCell, leftDownCell, board)) {
                return true;
            }

            return isCellCanAttackAnotherCell(currentCell, rightDownCell, board);
        }
    }
}
