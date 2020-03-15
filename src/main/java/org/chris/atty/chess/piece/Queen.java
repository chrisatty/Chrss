package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Queen extends Piece
{
    public Queen(Colour colour) {
        super(colour);
    }

    @Override
    public Set<Position> getValidMoves(Board board) {
        Set<Position> moves = MoveUtils.getDiagonalMoves(this, board);
        moves.addAll(MoveUtils.getStraightMoves(this, board));
        return moves;
    }

    @Override
    public String getName() {
        return "queen";
    }

    @Override
    public int getWorth() {
        return 8;
    }

}