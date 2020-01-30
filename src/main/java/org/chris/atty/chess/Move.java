package org.chris.atty.chess;

import org.chris.atty.chess.piece.*;

public class Move
{
    private final Piece piece;
    private final int x;
    private final int y;

    public Move(Piece piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = (int) (x ^ (x >>> 32));
        hash = 31 * hash + (int) (y ^ (y >>> 32));
        hash = 31 * hash + piece.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(Move.class)) {
            return false;
        }
        Move move = (Move) object;
        return move.getX() == x && move.getY() == y && move.getPiece().equals(piece);
    }

    @Override
    public String toString() {
        return piece.toString() + " -> " + x + "," + y;
    }
}