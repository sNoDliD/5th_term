package com.university.checkers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.university.checkers.checkers.Board;
import com.university.checkers.checkers.Cell;
import com.university.checkers.checkers.Checker;

import java.util.ArrayList;
import java.util.List;

public class CheckersActivity extends AppCompatActivity {

    private Board board;
    private ImageView[][] cellsPictures;
    private int[] imageViewIds;
    private List<Cell> highlightedCells;
    private List<Cell> playerCellsWithPriorityTurn;
    private boolean isFirstTap;
    private boolean isPlayersTurn;
    private boolean isPlayerAttacked;
    private Cell sourceCell;
    private Handler delayHandler;
    private Handler testHandler;
    private Cell tempCell;

    private final View.OnClickListener onClickListener = v -> {
        int tag = (Integer) v.getTag();
        int xCord = tag / 10;
        int yCord = tag % 10;

        try {
            doPlayerTurn(xCord, yCord);
        } catch (Exception e) {
            e.printStackTrace();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkers);

        delayHandler = new Handler();
        testHandler = new Handler(Looper.getMainLooper());
        isFirstTap = false;
        isPlayerAttacked = false;
        highlightedCells = null;
        playerCellsWithPriorityTurn = null;
        sourceCell = null;

        Bundle bundle = getIntent().getExtras();
        String chosenSide = bundle.getString("side");
        board = new Board(Utils.convertStringToBoolean(chosenSide));
        board.initializeBoard();
        isPlayersTurn = !Utils.convertStringToBoolean(chosenSide);
        cellsPictures = new ImageView[Board.BOARD_SIZE][Board.BOARD_SIZE];
        imageViewIds = getImageViewIdsArray();
        fillCellsPicturesArray(this.onClickListener);

        updateCheckersView();

        if (!isPlayersTurn) {
            try {
                doComputerTurn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static int[] getImageViewIdsArray() {
        return new int[]{R.id.cell01, R.id.cell03, R.id.cell05, R.id.cell07,
                R.id.cell10, R.id.cell12, R.id.cell14, R.id.cell16,
                R.id.cell21, R.id.cell23, R.id.cell25, R.id.cell27,
                R.id.cell30, R.id.cell32, R.id.cell34, R.id.cell36,
                R.id.cell41, R.id.cell43, R.id.cell45, R.id.cell47,
                R.id.cell50, R.id.cell52, R.id.cell54, R.id.cell56,
                R.id.cell61, R.id.cell63, R.id.cell65, R.id.cell67,
                R.id.cell70, R.id.cell72, R.id.cell74, R.id.cell76
        };
    }

    private void fillCellsPicturesArray(View.OnClickListener onClickListener) {

        int index = 0;

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if ((i + j) % 2 == 1) {
                    cellsPictures[i][j] = (ImageView) findViewById(imageViewIds[index]);
                    index++;
                    cellsPictures[i][j].setTag(i * 10 + j);
                    cellsPictures[i][j].setOnClickListener(onClickListener);
                }
            }
        }
    }

    public void removeChecker(Cell sourceCell) {
        int xCord = sourceCell.getxCord();
        int yCord = sourceCell.getyCord();

        cellsPictures[xCord][yCord].setImageResource(android.R.color.transparent);
    }

    public void updateCheckersView() {

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if ((i + j) % 2 == 1) {

                    //System.out.println("a im here");
                    Cell currCell = board.getCell(i, j);
                    Checker curr = currCell.getCurrentChecker();

                    if (curr != null) {
                        if (curr.getSide().equals(Checker.WHITE)) {
                            if (curr.isQueen()) {
                                cellsPictures[i][j].setImageResource(R.drawable.white_queen);
                            } else {
                                cellsPictures[i][j].setImageResource(R.drawable.white_checker);
                            }
                        } else {
                            if (curr.isQueen()) {
                                cellsPictures[i][j].setImageResource(R.drawable.black_queen);
                            } else {
                                cellsPictures[i][j].setImageResource(R.drawable.black_checker);
                            }
                        }
                    } else {
                        removeChecker(currCell);
                    }
                }
            }
        }
    }

    private void highlightChecker(int xCord, int yCord) {
        Cell currCell = board.getCell(xCord, yCord);
        Checker currChecker = board.obtainChecker(xCord, yCord);
        if (currChecker.isWhite()) {
            if (currChecker.isQueen()) {
                cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.white_queen_active);
            } else {
                cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.white_checker_active);
            }

            currChecker.setHighlighted(true);
            return;
        }

        if (currChecker.isQueen()) {
            cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.black_queen_active);
        } else {
            cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.black_checker_active);
        }

        currChecker.setHighlighted(true);
    }

    private void unhighlightChecker(int xCord, int yCord) {
        Cell currCell = board.getCell(xCord, yCord);
        Checker currChecker = board.obtainChecker(xCord, yCord);
        if (currChecker.isWhite()) {
            if (currChecker.isQueen()) {
                cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.white_queen);
            } else {
                cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.white_checker);
            }

            currChecker.setHighlighted(false);
            return;
        }

        if (currChecker.isQueen()) {
            cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.black_queen);
        } else {
            cellsPictures[currCell.getxCord()][currCell.getyCord()].setImageResource(R.drawable.black_checker);
        }

        currChecker.setHighlighted(false);
    }

    private void highlightCells(List<Cell> cellsToHighlight) {

        for (Cell cell : cellsToHighlight) {
            int xCord = cell.getxCord();
            int yCord = cell.getyCord();
            cellsPictures[xCord][yCord].setBackgroundResource(R.drawable.brown_square_active);
        }

        this.highlightedCells = cellsToHighlight;
    }

    private void unhighlightCells(List<Cell> cellsToUnhighlight) {

        for (Cell cell : cellsToUnhighlight) {
            int xCord = cell.getxCord();
            int yCord = cell.getyCord();
            cellsPictures[xCord][yCord].setBackgroundResource(R.drawable.brown_square);
        }

        this.highlightedCells = null;
    }

    private List<Cell> findPossibleMovesCells(Checker currChecker, boolean isComputerSide) {

        List<Cell> possibleMovesCells = new ArrayList<>();

        if (currChecker == null) {
            return possibleMovesCells;
        }

        Cell currCell = currChecker.getCoordinates();
        int xCord = currChecker.getCoordinates().getxCord();
        int yCord = currChecker.getCoordinates().getyCord();

        if (currChecker.isQueen()) {
            if (Utils.isQueenCellHasEnemyToAttack(currCell, board)) {
                return Utils.getAllPossibleMovesQueenAttack(currCell, board);
            } else {
                return Utils.getAllPossibleMovesQueenNoAttack(currCell, board);
            }
        } else {
            int xCordRight;
            int yCordRight;
            int xCordLeft;
            int yCordLeft;

            if (!isComputerSide) {
                xCordRight = xCord - 1;
                yCordRight = yCord + 1;
                xCordLeft = xCord - 1;
            } else {
                xCordRight = xCord + 1;
                yCordRight = yCord + 1;
                xCordLeft = xCord + 1;
            }
            yCordLeft = yCord - 1;


            Cell leftCell = board.getCell(xCordLeft, yCordLeft);
            Cell rightCell = board.getCell(xCordRight, yCordRight);

            if (leftCell != null) {
                if (leftCell.getCurrentChecker() == null) {
                    possibleMovesCells.add(leftCell);
                } else {
                    if (!leftCell.getCurrentChecker().getSide().equals(currChecker.getSide())) {

                        if (!isComputerSide) {
                            xCordLeft = xCordLeft - 1;
                        } else {
                            xCordLeft = xCordLeft + 1;
                        }
                        yCordLeft = yCordLeft - 1;

                        Cell newLeftCell = board.getCell(xCordLeft, yCordLeft);
                        if (newLeftCell != null) {
                            if (newLeftCell.getCurrentChecker() == null) {
                                possibleMovesCells.add(newLeftCell);
                            }
                        }
                    }
                }
            }

            if (rightCell != null) {
                if (rightCell.getCurrentChecker() == null) {
                    possibleMovesCells.add(rightCell);
                } else {
                    if (!rightCell.getCurrentChecker().getSide().equals(currChecker.getSide())) {
                        if (!isComputerSide) {
                            xCordRight = xCordRight - 1;
                        } else {
                            xCordRight = xCordRight + 1;
                        }
                        yCordRight = yCordRight + 1;


                        Cell newRightCell = board.getCell(xCordRight, yCordRight);
                        if (newRightCell != null) {
                            if (newRightCell.getCurrentChecker() == null) {
                                possibleMovesCells.add(newRightCell);
                            }
                        }
                    }
                }
            }

            //CHECK IF WE CAN BEAT REAR CHECKERS HAHA
            if (!isComputerSide) {
                xCordLeft = xCord + 1;
                xCordRight = xCord + 1;
            } else {
                xCordLeft = xCord - 1;
                xCordRight = xCord - 1;
            }
            yCordLeft = yCord - 1;
            yCordRight = yCord + 1;

            Cell leftRearCell = board.getCell(xCordLeft, yCordLeft);
            Cell rightRearCell = board.getCell(xCordRight, yCordRight);
            Cell currentCell = currChecker.getCoordinates();

            if (Utils.isCellCanAttackAnotherCell(currentCell, leftRearCell, board)) {
                Cell rearCell = Utils.findRearCellAttack(currentCell, leftRearCell, board);
                if (rearCell != null) {
                    possibleMovesCells.add(rearCell);
                }
            }

            if (Utils.isCellCanAttackAnotherCell(currentCell, rightRearCell, board)) {
                Cell rearCell = Utils.findRearCellAttack(currentCell, rightRearCell, board);
                if (rearCell != null) {
                    possibleMovesCells.add(rearCell);
                }
            }
        }

        List<Cell> realPossibleMovesCells = new ArrayList<>();
        for (Cell possibleMovesCell : possibleMovesCells) {
            //IF THEY ARE NOT NEIGHBOURS => CHECKER CAN BEAT ANOTHER CHECKER => PRIORITY TURN
            if (!Utils.areCellsAreNeighbours(possibleMovesCell, currCell)) {
                realPossibleMovesCells.add(possibleMovesCell);
            }
        }

        if (realPossibleMovesCells.size() > 0) {
            return realPossibleMovesCells;
        } else {
            return possibleMovesCells;
        }
    }

    private Cell findSourceCellForComputer() {

        List<Checker> computerCheckers = board.obtainAllComputerSideCheckers();
        Cell outputCell = null;

        for (Checker currentChecker : computerCheckers) {
            Cell currentCell = currentChecker.getCoordinates();

            List<Cell> currentPossibleMoves = findPossibleMovesCells(currentChecker, true);
            if (currentPossibleMoves.size() > 0) {
                outputCell = currentCell;
                for (Cell currentPossibleMove : currentPossibleMoves) {
                    //IT MEANS THAT CELLS ARE NOT NEIGHBOURS => CELL CAN BEAT ANOTHER CELL => PRIORITY TURN
                    if (!Utils.areCellsAreNeighbours(currentCell, currentPossibleMove)) {
                        return outputCell;
                    }
                }
            }
        }

        return outputCell;
    }

    private List<Cell> findCellsThatCanAttackAnother(String side) {
        //ONLY FOR PLAYER
        List<Cell> outputList = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                Cell currCell = board.getCell(i, j);
                Checker currChecker = currCell.getCurrentChecker();
                if (currChecker != null && currChecker.getSide().equals(board.getPlayerSideStr())) {
                    if (Utils.isCellHasEnemyToAttack(currCell, board)) {
                        outputList.add(currCell);
                    }
                }
            }
        }

        playerCellsWithPriorityTurn = outputList;
        return outputList;
    }

    private Checker findHighlightedChecker() {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board.obtainChecker(i, j) != null && board.obtainChecker(i, j).isHighlighted()) {
                    return board.obtainChecker(i, j);
                }
            }
        }

        return null;
    }

    private boolean doCheckerMove(Cell sourceCell, Cell destCell, boolean isComputerTurn) throws Exception {
        return board.doMove(sourceCell, destCell, isComputerTurn);
    }

    public void doPlayerTurn(int xCord, int yCord) throws Exception {
        //IF COMPUTER IS CURRENTLY DOING A TURN
        if (!this.isPlayersTurn) {
            return;
        }

        if (!this.isFirstTap) {
            int gameState = checkGameWinner();

            if (gameState == 0) {
                Cell currentCell = board.getCell(xCord, yCord);
                Checker currentChecker = currentCell.getCurrentChecker();
                if (currentChecker == null) {
                    return;
                }

                //IF PLAYER HAS CHECKER TO BEAT BUT WANTS TO MAKE ANOTHER TURN
                findCellsThatCanAttackAnother(currentChecker.getSide());
                if (playerCellsWithPriorityTurn != null && playerCellsWithPriorityTurn.size() > 0) {
                    if (!playerCellsWithPriorityTurn.contains(currentCell)) {
                        Checker highlightedChecker = findHighlightedChecker();

                        if (highlightedChecker != null) {
                            unhighlightChecker(highlightedChecker.getCoordinates().getxCord(), highlightedChecker.getCoordinates().getyCord());
                        }

                        if (this.highlightedCells != null) {
                            unhighlightCells(highlightedCells);
                        }

                        return;
                    }
                }

                //IF PLAYER CHOSE HIS OWN SIDE CHECKER
                if (currentChecker.getSide().equals(board.getPlayerSideStr())) {
                    Checker highlightedChecker = findHighlightedChecker();

                    if (highlightedChecker != null) {
                        unhighlightChecker(highlightedChecker.getCoordinates().getxCord(), highlightedChecker.getCoordinates().getyCord());
                    }

                    if (this.highlightedCells != null) {
                        unhighlightCells(highlightedCells);
                    }

                    highlightChecker(xCord, yCord);
                    highlightCells(findPossibleMovesCells(currentChecker, false));

                    this.isFirstTap = true;
                    this.sourceCell = currentCell;
                }
            } else {
                endGame(gameState);
            }
        } else {
            Cell currentCell = board.getCell(xCord, yCord);
            Checker currentChecker = currentCell.getCurrentChecker();

            //IF THE PLAYER WANTS TO DO A MOVE
            if (this.highlightedCells.contains(currentCell)) {

                unhighlightCells(highlightedCells);

                if (doCheckerMove(this.sourceCell, currentCell, false)) {
                    if (Utils.isCellHasEnemyToAttack(currentCell, board)) {
                        isFirstTap = false;
                        sourceCell = null;

                        updateCheckersView();

                        return;
                    }
                }

                isFirstTap = false;
                sourceCell = null;

                updateCheckersView();

                this.isPlayersTurn = false;
                doComputerTurn();

            } else {    //IF A PLAYER TAPS SOMETHING DIFFERENT

                if (currentChecker != null && currentChecker.isHighlighted()) {
                    unhighlightChecker(xCord, yCord);
                    unhighlightCells(this.highlightedCells);
                    isFirstTap = false;
                } else {
                    if (!currentCell.containsChecker()) {
                        return;
                    }

                    if (currentChecker != null && currentChecker.getSide().equals(board.getComputerSideStr())) {
                        return;
                    }

                    isFirstTap = false;
                    doPlayerTurn(xCord, yCord);
                }

            }
        }
    }

    public void doComputerTurn() throws Exception {

        int gameState = checkGameWinner();

        if (gameState != 0) {
            endGame(gameState);
            return;
        }

        delayHandler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void run() {
                Cell sourceCell = findSourceCellForComputer();
                Cell destCell = null;
                if (sourceCell.getCurrentChecker() == null) {
                    System.out.println("Unexpected Error");
                    return;
                }
                List<Cell> possibleMoves = findPossibleMovesCells(sourceCell.getCurrentChecker(), true);

                for (int i = 0; i < possibleMoves.size(); i++) {
                    destCell = possibleMoves.get(i);
                    if (!Utils.areCellsAreNeighbours(sourceCell, destCell)) {
                        break;
                    }
                }

                try {
                    doCheckerMove(sourceCell, destCell, true);

                    //we are checking if we did the attack on the checker last moment
                    if (!Utils.areCellsAreNeighbours(sourceCell, destCell)) {
                        while (Utils.isCellHasEnemyToAttack(destCell, board)) {
                            doComputerTurn(destCell);
                            destCell = tempCell;
                        }
                    }

                    isPlayersTurn = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateCheckersView();
            }
        }, 1000);

        if (gameState != 0) {
            endGame(gameState);
        }
    }

    //WHEN COMPUTER CONSEQUENTLY ATTACKS CHECKER BY CHECKER
    public void doComputerTurn(Cell sourceCell) {
        testHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Cell> possibleMoves = findPossibleMovesCells(sourceCell.getCurrentChecker(), true);

                for (int i = 0; i < possibleMoves.size(); i++) {

                    tempCell = possibleMoves.get(i);
                    if (!Utils.areCellsAreNeighbours(sourceCell, tempCell)) {
                        break;
                    }
                }

                try {
                    doCheckerMove(sourceCell, tempCell, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateCheckersView();

                if (Utils.isCellHasEnemyToAttack(tempCell, board)) {
                    doComputerTurn(tempCell);
                }
            }
        }, 300);

    }


    /**
     * @return 0 - game is still active; 1 - white side wins; 2 - black side wins; -1 - draw
     */
    public int checkGameWinner() {
        int whiteAmount = board.getWhiteCheckersAmount();
        int blackAmount = board.getBlackCheckersAmount();

        if (whiteAmount <= 0 && blackAmount <= 0) {
            return -1;
        }
        if (whiteAmount > 0 && blackAmount > 0) {
            return 0;
        }
        if (whiteAmount > 0) {
            return 1;
        }
        return 2;
    }

    public void endGame(int gameState) {

        Intent intent = new Intent(this, MainActivity.class);
        String result;

        switch (gameState) {
            case 1: {
                result = "White side won last game";
                break;
            }
            case 2: {
                result = "Black side won last game";
                break;
            }
            case -1: {
                result = "Last game was finished with draw";
                break;
            }
            default: {
                System.out.println("UNEXPECTED ERROR");
                return;
            }
        }

        intent.putExtra("result", result);
        startActivity(intent);
    }
}