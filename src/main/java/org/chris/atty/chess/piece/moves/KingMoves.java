package org.chris.atty.chess.piece.moves;

import java.util.Set;

import org.chris.atty.chess.Board;
import org.chris.atty.chess.Move;
import org.chris.atty.chess.piece.King;

public class KingMoves extends MoveGenerator<King>
{

    public KingMoves(King piece, Board board) {
        super(piece, board);
    }

    public Set<Move> get() {
        return null;
    }

}