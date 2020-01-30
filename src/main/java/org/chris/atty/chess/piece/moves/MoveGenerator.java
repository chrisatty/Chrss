package org.chris.atty.chess.piece.moves;

import java.util.Set;

import org.chris.atty.chess.Board;
import org.chris.atty.chess.Move;
import org.chris.atty.chess.piece.Piece;

public abstract class MoveGenerator<T extends Piece>
{
    private T piece;
    private Board board;

    public MoveGenerator(T piece, Board board) {
        this.piece = piece;
        this.board = board;
    }

    public abstract Set<Move> get(); 

}