package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public abstract class Piece implements Cloneable
{
    protected final Colour colour;
    protected Position position;
    protected int numMoves = 0;

    public Piece(Colour colour, Position position) {
        this.position = position;
        this.colour = colour;
    }

    public Position getPosition() {
        return position;
    }

    public void move(Position newPosition) {
        if (!position.equals(newPosition)) {
            numMoves++;
            this.position = newPosition;
        }
    }

    public Colour getColour() {
        return colour;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public abstract Set<Move> getValidMoves(Board board);
    public abstract String getName();
    public abstract int getWorth();
    public abstract Piece clone();

    @Override
    public String toString() {
        return getName() + "[" + getColour() + "] at " + "(" + position + ")";
    }

    public String icon() {
        return getName().charAt(0) + (colour.equals(Colour.BLACK) ? "'" : " ");
    }

    @Override
    public int hashCode() {
        int hash = 31 * position.hashCode();
        hash = 31 * hash + colour.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())) {
            return false;
        }
        Piece piece = (Piece) object;
        return piece.getPosition().equals(position) && piece.getColour().equals(colour);
    }
}