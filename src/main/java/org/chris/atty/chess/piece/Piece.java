package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public abstract class Piece implements Cloneable
{
    protected final Colour colour;
    protected int numMoves = 0;

    public Piece(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void incMoveCount() {
        numMoves++;
    }

    public abstract Set<Position> getValidMoves(Board board);
    public abstract String getName();
    public abstract int getWorth();

    @Override
    public String toString() {
        return getName() + "[" + getColour() + "]";
    }

    public String icon() {
        return getName().charAt(0) + (colour.equals(Colour.BLACK) ? "'" : " ");
    }
}