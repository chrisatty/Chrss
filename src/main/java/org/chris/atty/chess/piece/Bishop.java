package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Bishop extends Piece
{
    public Bishop(Colour colour) {
        super(colour);
    }

    @Override
    public Set<Position> getValidMoves(Board board) {
        return MoveUtils.getDiagonalMoves(this, board);
    }

    @Override
    public String getName() {
        return "bishop";
    }

    @Override
    public int getWorth() {
        return 3;
    }
}