package org.chris.atty.chess.piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.*;

public class Pawn extends Piece
{
    private boolean firstMove = true;
    private final int initialY;

    public Pawn(Colour colour, int x, int y) {
        super(colour, x, y);
        initialY = y;
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        int x = currentX;
        int y = currentY;
        Set<Move> moves = new HashSet<>();
        int m = colour.equals(Colour.WHITE) ? 1 : -1;

        // move
        if (board.inBounds(x, y+1*m) && !boardArray[x][y+1*m].isPresent()) {
            moves.add(new Move(this, x, y + 1*m));
            if (firstMove && board.inBounds(x, y+2*m) && !boardArray[x][y+2*m].isPresent()) {
                moves.add(new Move(this, x, y+2*m));
            }
        }
        
        // taking
        if (MoveUtils.canMoveTo(this, x-1, y + (1*m), boardArray)) {
            moves.add(new Move(this, x-1, y + (1*m)));
        }
        if (MoveUtils.canMoveTo(this, x+1, y + (1*m), boardArray)) {
            moves.add(new Move(this, x+1, y + (1*m)));
        }
        return moves;
    }

    @Override
    public void move(int x, int y) {
        firstMove = false;
        super.move(x, y);
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
        Pawn pawn = new Pawn(colour, currentX, currentY);
        pawn.firstMove = this.firstMove;
        return pawn;
    }
}