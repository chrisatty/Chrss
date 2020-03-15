package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Pawn extends Piece
{

    public Pawn(Colour colour) {
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
        int m = colour.equals(Colour.WHITE) ? 1 : -1;

        // move
        if (board.inBounds(Position.fromCoords(x, y+1*m)) && !boardArray[x][y+1*m].isPresent()) {
            moves.add(Position.fromCoords(x, y + 1*m));
            if (numMoves == 0 && !boardArray[x][y+2*m].isPresent()) {
                moves.add(Position.fromCoords(x, y+2*m));
            }
        }
        
        // taking
        if (MoveUtils.canMoveTo(this, x-1, y + (1*m), boardArray)) {
            moves.add(Position.fromCoords(x-1, y + (1*m)));
        }
        if (MoveUtils.canMoveTo(this, x+1, y + (1*m), boardArray)) {
            moves.add(Position.fromCoords(x+1, y + (1*m)));
        }
        return moves;
    }

    @Override
    public String getName() {
        return "pawn";
    }

    @Override
    public int getWorth() {
        return 1;
    }
}