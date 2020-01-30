package org.chris.atty.chess;

import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.chris.atty.chess.piece.*;

public class Board implements Cloneable
{
    public final int HEIGHT = 8;
    public final int WIDTH = 8;

    private Set<Piece> pieces = new HashSet<>();

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public Optional<Piece> move(Piece piece, int x, int y) {
        Optional<Piece> pieceToRemove = get(x, y);
        if (pieceToRemove.isPresent()) {
            pieces.remove(pieceToRemove.get());
        }
        piece.move(x, y);
        return pieceToRemove;
    }

    // used when pawns -> queen
    public void replace(Piece oldPiece, Piece newPiece) {
        pieces.remove(oldPiece);
        pieces.add(newPiece);
    }

    public Optional<Piece> get(int x, int y) {
        return pieces.stream().filter(p -> p.getX() == x && p.getY() == y).findAny();
    }

    public Set<Piece> getPieces(Colour colour) {
        return pieces.stream().filter(p -> p.getColour().equals(colour)).collect(Collectors.toSet());
    }

    public Set<Piece> getAllPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        if (get(piece.getX(), piece.getY()).isPresent()) {
            System.out.println("Old piece " + get(piece.getX(), piece.getY()).get().toString());
            System.out.println("New piece " + piece.toString());
            throw new IllegalArgumentException("Piece already exists in location");
        }
        pieces.add(piece);
    }

    @Override
    public Board clone() {
        Board clone = new Board();
        pieces.forEach(piece -> clone.addPiece(piece.clone()));
        return clone;
    }
}