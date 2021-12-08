package com.university.checkers.checkers;

public class Checker {

    public static final String BLACK = "black";
    public static final String WHITE = "white";

    private boolean isQueen;
    private boolean isHighlighted;
    private String side;
    private Cell coordinates;

    public Checker(String side){
        this.side = side;
        this.isQueen = false;
        this.coordinates = null;
        this.isHighlighted = false;
    }

    public Checker(String side, Cell coordinates){
        this.side = side;
        this.isQueen = false;
        this.coordinates = coordinates;
        this.isHighlighted = false;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Cell getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Cell coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isWhite() {
        return side.equals(WHITE);
    }
}
