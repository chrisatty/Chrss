package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Bishop extends Piece
{
    public Bishop(Colour colour, int x, int y) {
        super(colour, x, y);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
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

    @Override
    public Bishop clone() {
        return new Bishop(colour, currentX, currentY);
    }
}