package org.chris.atty.chess.piece;

import java.util.Set;

import org.chris.atty.chess.*;

public class Rook extends Piece
{
    // required for castling
    private boolean firstMove = true;

    public Rook(Colour colour, int x, int y) {
        super(colour, x, y);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        return MoveUtils.getStraightMoves(this, board);
    }

    @Override
    public void move(int x, int y) {
        firstMove = false;
        super.move(x, y);
    }

    @Override
    public String getName() {
        return "rook";
    }

    @Override
    public int getWorth() {
        return 5;
    }

    public boolean canCastle() {
        return firstMove;
    }

    @Override
    public Rook clone() {
        Rook rook = new Rook(colour, currentX, currentY);
        rook.firstMove = this.firstMove;
        return rook;
    }
}