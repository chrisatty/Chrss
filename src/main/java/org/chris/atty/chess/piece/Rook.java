package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Rook extends Piece
{
    public Rook(Colour colour) {
        super(colour);
    }

    @Override
    public Set<Position> getValidMoves(Board board) {
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
}