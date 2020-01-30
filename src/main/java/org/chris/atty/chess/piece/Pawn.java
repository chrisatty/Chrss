package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Pawn extends Piece
{
    private boolean firstMove = true;

    public Pawn(Colour colour, Position position) {
        super(colour, position);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        int x = position.getX();
        int y = position.getY();
        Set<Move> moves = new HashSet<>();
        int m = colour.equals(Colour.WHITE) ? 1 : -1;

        // move
        if (board.inBounds(Position.fromCoords(x, y+1*m)) && !boardArray[x][y+1*m].isPresent()) {
            moves.add(new Move(this, Position.fromCoords(x, y + 1*m)));
            if (firstMove && !boardArray[x][y+2*m].isPresent()) {
                moves.add(new Move(this, Position.fromCoords(x, y+2*m)));
            }
        }
        
        // taking
        if (MoveUtils.canMoveTo(this, x-1, y + (1*m), boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x-1, y + (1*m))));
        }
        if (MoveUtils.canMoveTo(this, x+1, y + (1*m), boardArray)) {
            moves.add(new Move(this, Position.fromCoords(x+1, y + (1*m))));
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

    @Override
    public Pawn clone() {
        Pawn clone = new Pawn(colour, position);
        clone.numMoves = numMoves;
        return clone;
    }
}