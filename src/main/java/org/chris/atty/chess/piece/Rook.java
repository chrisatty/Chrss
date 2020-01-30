package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Rook extends Piece
{
    public Rook(Colour colour, Position position) {
        super(colour, position);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        return MoveUtils.getStraightMoves(this, board);
    }

    @Override
    public String getName() {
        return "rook";
    }

    @Override
    public int getWorth() {
        return 5;
    }

    @Override
    public Rook clone() {
        Rook clone = new Rook(colour, position);
        clone.numMoves = numMoves;
        return clone;
    }
}