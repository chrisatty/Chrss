package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Knight extends Piece
{
    public Knight(Colour colour, int x, int y) {
        super(colour, x, y);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();
        int x = currentX;
        int y = currentY;
        if (MoveUtils.canMoveTo(this, x+1, y+2, boardArray)) {
            moves.add(new Move(this, x+1, y+2));
        }

        if (MoveUtils.canMoveTo(this, x-1, y+2, boardArray)) {
            moves.add(new Move(this, x-1, y+2));
        }

        if (MoveUtils.canMoveTo(this, x+1, y-2, boardArray)) {
            moves.add(new Move(this, x+1, y-2));
        }

        if (MoveUtils.canMoveTo(this, x-1, y-2, boardArray)) {
            moves.add(new Move(this, x-1, y-2));
        }

        if (MoveUtils.canMoveTo(this, x+2, y+1, boardArray)) {
            moves.add(new Move(this, x+2, y+1));
        }

        if (MoveUtils.canMoveTo(this, x-2, y+1, boardArray)) {
            moves.add(new Move(this, x-2, y+1));
        }

        if (MoveUtils.canMoveTo(this, x+2, y-1, boardArray)) {
            moves.add(new Move(this, x+2, y-1));
        }

        if (MoveUtils.canMoveTo(this, x-2, y-1, boardArray)) {
            moves.add(new Move(this, x-2, y-1));
        }
        return moves;
    }

    @Override
    public String getName() {
        return "knight";
    }

    @Override
    public int getWorth() {
        return 3;
    }

    @Override
    public Knight clone() {
        return new Knight(colour, currentX, currentY);
    }
}