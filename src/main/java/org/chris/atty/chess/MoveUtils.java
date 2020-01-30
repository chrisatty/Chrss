package org.chris.atty.chess;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.chris.atty.chess.piece.Piece;

public class MoveUtils {

    public static Set<Move> getDiagonalMoves(Piece piece, Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();

        int x = piece.getPosition().getX() + 1;
        int y = piece.getPosition().getY() + 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(new Move(piece, Position.fromCoords(x, y)));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x++; y++;
        }

        x = piece.getPosition().getX() + 1;
        y = piece.getPosition().getY() - 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(new Move(piece, Position.fromCoords(x, y)));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x++; y--;
        }

        x = piece.getPosition().getX() - 1;
        y = piece.getPosition().getY() + 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(new Move(piece, Position.fromCoords(x, y)));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x--; y++;
        }

        x = piece.getPosition().getX() - 1;
        y = piece.getPosition().getY() - 1;
        while (canMoveTo(piece, x, y, boardArray)) {
            moves.add(new Move(piece, Position.fromCoords(x, y)));
            if (boardArray[x][y].isPresent()) {
                break;
            }
            x--; y--;
        }
        return moves;
    }

    public static Set<Move> getStraightMoves(Piece piece, Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();
        int currentX = piece.getPosition().getX();
        int currentY = piece.getPosition().getY();

        for (int x = currentX + 1; x < board.WIDTH; x++) {
            if (canMoveTo(piece, x, currentY, boardArray)) {
                moves.add(new Move(piece, Position.fromCoords(x, currentY)));
                if (boardArray[x][currentY].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int x = currentX - 1; x >= 0; x--) {
            if (canMoveTo(piece, x, currentY, boardArray)) {
                moves.add(new Move(piece, Position.fromCoords(x, currentY)));
                if (boardArray[x][currentY].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int y = currentY + 1; y < board.HEIGHT; y++) {
            if (canMoveTo(piece, currentX, y, boardArray)) {
                moves.add(new Move(piece,  Position.fromCoords(currentX, y)));
                if (boardArray[currentX][y].isPresent()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (int y = currentY - 1; y >= 0; y--) {
            if (canMoveTo(piece, currentX, y, boardArray)) {
                moves.add(new Move(piece,  Position.fromCoords(currentX, y)));
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