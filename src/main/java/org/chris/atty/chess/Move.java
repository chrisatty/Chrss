package org.chris.atty.chess;

import org.chris.atty.chess.piece.*;

public class Move
{
    private final Piece piece;
    private final Position position;
    private final boolean isCastle;

    public Move(Piece piece, Position position) {
        this.piece = piece;
        this.position = position;
        this.isCastle = Move.isCastle(this);
    }

    public Piece getPiece() {
        return piece;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public boolean isPawnPromote() {
        return piece.getClass().equals(Pawn.class) && (position.getRank() == 1 || position.getRank() == 8);
    }

    @Override
    public int hashCode() {
        int hash = 31 * position.hashCode();
        hash = 31 * hash + piece.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(Move.class)) {
            return false;
        }
        Move move = (Move) object;
        return move.getPosition().equals(position) && move.getPiece().equals(piece);
    }

    @Override
    public String toString() {
        return piece.icon() + "-> " + position;
    }

    // is this move a castle?
    private static boolean isCastle(Move move) {
        Piece movingPiece = move.getPiece();
        Position currentPosition = movingPiece.getPosition();
        Position newPosition = move.getPosition();
        return movingPiece.getClass().equals(King.class) &&
            currentPosition.getRank() == newPosition.getRank() &&
            Math.abs(currentPosition.getFile() - newPosition.getFile()) > 1;
    }
}