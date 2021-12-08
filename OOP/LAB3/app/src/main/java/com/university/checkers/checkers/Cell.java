package com.university.checkers.checkers;

public class Cell {

    private int xCord;
    private int yCord;
    private int cellNumber;

    private Checker currentChecker;

    public Cell(){
        xCord = 0;
        yCord = 0;
        currentChecker = null;
        cellNumber = 0;
    }

    public Cell(int xCord, int yCord, int cellNumber){
        this.xCord = xCord;
        this.yCord = yCord;
        currentChecker = null;
        this.cellNumber = cellNumber;
    }

    public int getxCord() {
        return xCord;
    }

    public void setxCord(int xCord) {
        this.xCord = xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public void setyCord(int yCord) {
        this.yCord = yCord;
    }

    public Checker getCurrentChecker() {
        return currentChecker;
    }

    public void setCurrentChecker(Checker currentChecker) {
        this.currentChecker = currentChecker;
    }

    public boolean containsChecker(){
        return currentChecker != null;
    }

    public void removeChecker() {
        this.currentChecker = null;
    }
}
