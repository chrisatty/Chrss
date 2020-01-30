package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public abstract class Piece implements Cloneable
{
    protected final Colour colour;
    protected int currentX;
    protected int currentY;

    public Piece(Colour colour, int x, int y) {
        this.currentX = x;
        this.currentY = y;
        this.colour = colour;
    }

    public int getX() {
        return currentX;
    }

    public int getY() {
        return currentY;
    }

    public void move(int x, int y) {
        this.currentX = x;
        this.currentY = y;
    }

    public Colour getColour() {
        return colour;
    }

    public abstract Set<Move> getValidMoves(Board board);
    public abstract String getName();
    public abstract int getWorth();
    public abstract Piece clone();

    @Override
    public String toString() {
        return getName() + "[" + getColour() + "] at " + "(" + getX() + "," + getY() + ")";
    }

    public String icon() {
        return getName().charAt(0) + (colour.equals(Colour.BLACK) ? "'" : " ");
    }

    @Override
    public int hashCode() {
        int hash = (int) (currentX ^ (currentX >>> 32));
        hash = 31 * hash + (int) (currentY ^ (currentY >>> 32));
        hash = 31 * hash + colour.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())) {
            return false;
        }
        Piece piece = (Piece) object;
        return piece.getX() == currentX && piece.getY() == currentY && piece.getColour().equals(colour);
    }
}