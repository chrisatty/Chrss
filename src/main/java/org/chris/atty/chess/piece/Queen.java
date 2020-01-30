package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Queen extends Piece
{
    public Queen(Colour colour, int x, int y) {
        super(colour, x, y);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Set<Move> moves = MoveUtils.getDiagonalMoves(this, board);
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

    @Override
    public Queen clone() {
        return new Queen(colour, currentX, currentY);
    }
}