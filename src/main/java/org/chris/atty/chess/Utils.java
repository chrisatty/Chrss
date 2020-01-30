package org.chris.atty.chess;

import java.util.Optional;

import org.chris.atty.chess.piece.Piece;

public class Utils {

    public static Optional<Piece>[][] toArray(Board origBoard) {
        Optional<Piece>[][] board = (Optional<Piece>[][]) new Optional<?>[origBoard.WIDTH][origBoard.HEIGHT];
        for (int x = 0; x < origBoard.WIDTH; x++) {
            for (int y = 0; y < origBoard.HEIGHT; y++) {
                board[x][y] = origBoard.get(Position.fromCoords(x, y));
            }
        }
        return board;
    }
}