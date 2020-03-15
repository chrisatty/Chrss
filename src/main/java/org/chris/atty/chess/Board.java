package org.chris.atty.chess;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.chris.atty.chess.piece.Piece;

public class Board implements Cloneable
{
    public final int HEIGHT = 8;
    public final int WIDTH = 8;

    private final Map<Position, Optional<Piece>> board;

    public Board() {
        board = new HashMap<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y=0; y < HEIGHT; y++) {
                board.put(Position.fromCoords(x, y), Optional.empty());
            }
        }
    }

    private Board(Map<Position, Optional<Piece>> board) {
        this.board = board;
    }


    public boolean inBounds(Position pos) {
        return board.containsKey(pos);
    }

    public Optional<Piece> move(Position oldPosition, Position newPosition) {
        if (!inBounds(oldPosition) || !inBounds(newPosition)) {
            throw new IllegalArgumentException("Invalid position on board");
        }
        Optional<Piece> piece = board.get(oldPosition);
        if (!piece.isPresent()) {
            throw new IllegalArgumentException("No piece at position " + oldPosition);
        }
        board.put(oldPosition, Optional.empty());
        return board.put(newPosition, piece);
    }

    public Optional<Position> find(Piece piece) {
        return board.keySet().stream()
                            .filter(p -> board.get(p).isPresent() && board.get(p).get().equals(piece))
                            .findAny();
    }

    public Optional<Piece> get(Position position) {
        return board.get(position);
    }

    public Set<Piece> getPieces(Colour colour) {
        return board.keySet().stream()
                            .filter(p -> board.get(p).isPresent())
                            .map(p -> board.get(p).get())
                            .filter(p -> p.getColour().equals(colour))
                            .collect(Collectors.toSet());
    }

    public Set<Piece> getAllPieces() {
        return board.keySet().stream()
                            .filter(p -> board.get(p).isPresent())
                            .map(p -> board.get(p).get())
                            .collect(Collectors.toSet());
    }

    public Optional<Piece> add(Piece piece, Position position) {
        if (!board.containsKey(position)) {
            throw new IllegalArgumentException("Position does not exist on board");
        }
        return board.put(position, Optional.of(piece));
    }

    @Override
    public Board clone() {
        return new Board(new HashMap<>(board));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("     A   B   C   D   E   F   G   H    ");
        builder.append(System.lineSeparator());
        for (int y = 8; y >= 1 ; y--) {
            builder.append("   ---------------------------------   ");
            builder.append(System.lineSeparator());
            builder.append(" " + y + " ");
            for (char x = 'A'; x <= 'H'; x++) {
                Optional<Piece> piece = get(new Position(x, y));
                if (piece.isPresent()) {
                    builder.append("| " + piece.get().icon());
                } else {
                    builder.append("|   ");
                }
                
            }
            builder.append("|");
            builder.append(" " + y + " ");
            builder.append(System.lineSeparator());
        }
        builder.append("   ---------------------------------   ");
        builder.append(System.lineSeparator());
        builder.append("     A   B   C   D   E   F   G   H    ");
        return builder.toString();
    }

    public Optional<Piece>[][] toArray() {
        Optional<Piece>[][] boardArray = (Optional<Piece>[][]) new Optional<?>[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                boardArray[x][y] = board.get(Position.fromCoords(x, y));
            }
        }
        return boardArray;
    }
}