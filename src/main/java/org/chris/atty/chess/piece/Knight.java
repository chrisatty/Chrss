package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Knight extends Piece
{
    public Knight(Colour colour, Position position) {
        super(colour, position);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();
        int x = position.getX();
        int y = position.getY();
        if (MoveUtils.canMoveTo(this, x+1, y+2, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x+1, y+2)));
        }

        if (MoveUtils.canMoveTo(this, x-1, y+2, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x-1, y+2)));
        }

        if (MoveUtils.canMoveTo(this, x+1, y-2, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x+1, y-2)));
        }

        if (MoveUtils.canMoveTo(this, x-1, y-2, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x-1, y-2)));
        }

        if (MoveUtils.canMoveTo(this, x+2, y+1, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x+2, y+1)));
        }

        if (MoveUtils.canMoveTo(this, x-2, y+1, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x-2, y+1)));
        }

        if (MoveUtils.canMoveTo(this, x+2, y-1, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x+2, y-1)));
        }

        if (MoveUtils.canMoveTo(this, x-2, y-1, boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x-2, y-1)));
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
        Knight clone = new Knight(colour, position);
        clone.numMoves = numMoves;
        return clone;
    }
}