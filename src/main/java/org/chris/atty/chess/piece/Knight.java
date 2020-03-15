package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Knight extends Piece
{
    public Knight(Colour colour) {
        super(colour);
    }

    @Override
    public Set<Position> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = board.toArray();
        Set<Position> moves = new HashSet<>();
        if (!board.find(this).isPresent()) {
            return moves;
        }
        Position currentPosition = board.find(this).get();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        if (MoveUtils.canMoveTo(this, x+1, y+2, boardArray)) {
            moves.add(Position.fromCoords(x+1, y+2));
        }

        if (MoveUtils.canMoveTo(this, x-1, y+2, boardArray)) {
            moves.add(Position.fromCoords(x-1, y+2));
        }

        if (MoveUtils.canMoveTo(this, x+1, y-2, boardArray)) {
            moves.add(Position.fromCoords(x+1, y-2));
        }

        if (MoveUtils.canMoveTo(this, x-1, y-2, boardArray)) {
            moves.add(Position.fromCoords(x-1, y-2));
        }

        if (MoveUtils.canMoveTo(this, x+2, y+1, boardArray)) {
            moves.add(Position.fromCoords(x+2, y+1));
        }

        if (MoveUtils.canMoveTo(this, x-2, y+1, boardArray)) {
            moves.add(Position.fromCoords(x-2, y+1));
        }

        if (MoveUtils.canMoveTo(this, x+2, y-1, boardArray)) {
            moves.add(Position.fromCoords(x+2, y-1));
        }

        if (MoveUtils.canMoveTo(this, x-2, y-1, boardArray)) {
            moves.add(Position.fromCoords(x-2, y-1));
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
}