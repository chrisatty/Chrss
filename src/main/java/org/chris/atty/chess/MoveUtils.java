package org.chris.atty.chess;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.piece.Piece;

public class MoveUtils {

    public static Set<Position> getDiagonalMoves(Piece piece, Board board) {
        Optional<Piece>[][] boardArray = board.toArray();
        Set<Position> moves = new HashSet<>();
        if (!board.find(piece).isPresent()) {
            return moves;
        }
        Position currentPosition = board.find(piece).get();

        int x = currentPosition.getX() + 1;
        int y = currentPosition.getY() + 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(Position.fromCoords(x, y));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x++; y++;
        }

        x = currentPosition.getX() + 1;
        y = currentPosition.getY() - 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(Position.fromCoords(x, y));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x++; y--;
        }

        x = currentPosition.getX() - 1;
        y = currentPosition.getY() + 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(Position.fromCoords(x, y));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x--; y++;
        }

        x = currentPosition.getX() - 1;
        y = currentPosition.getY() - 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(Position.fromCoords(x, y));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x--; y--;
        }
        return moves;
    }

    public static Set<Position> getStraightMoves(Piece piece, Board board) {
        Optional<Piece>[][] boardArray = board.toArray();
        Set<Position> moves = new HashSet<>();
        if (!board.find(piece).isPresent()) {
            return moves;
        }
        Position currentPosition = board.find(piece).get();

        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();

        for (int x = currentX + 1; x < board.WIDTH; x++) {
            if (canMoveTo(piece, x, currentY, boardArray)) {
                moves.add(Position.fromCoords(x, currentY));
                if (boardArray[x][currentY].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int x = currentX - 1; x >= 0; x--) {
            if (canMoveTo(piece, x, currentY, boardArray)) {
                moves.add(Position.fromCoords(x, currentY));
                if (boardArray[x][currentY].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int y = currentY + 1; y < board.HEIGHT; y++) {
            if (canMoveTo(piece, currentX, y, boardArray)) {
                moves.add(Position.fromCoords(currentX, y));
                if (boardArray[currentX][y].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int y = currentY - 1; y >= 0; y--) {
            if (canMoveTo(piece, currentX, y, boardArray)) {
                moves.add(Position.fromCoords(currentX, y));
                if (boardArray[currentX][y].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public static boolean canMoveTo(Piece piece, int x, int y, Optional<Piece>[][] board) {
        return x >= 0 && y >= 0 && x < board.length && y < board[x].length &&
             (!board[x][y].isPresent() || !board[x][y].get().getColour().equals(piece.getColour()));
    }
}