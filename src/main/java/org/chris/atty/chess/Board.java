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

    public boolean inBounds(Position pos) {
        return pos.getX() >= 0 && pos.getX() < WIDTH && pos.getY() >= 0 && pos.getY() < HEIGHT;
    }

    public Optional<Piece> move(Piece piece, Position position) {
        Optional<Piece> pieceToRemove = get(position);
        if (pieceToRemove.isPresent()) {
            pieces.remove(pieceToRemove.get());
        }
        piece.move(position);
        return pieceToRemove;
    }

    // used when pawns -> queen
    public void replace(Piece oldPiece, Piece newPiece) {
        pieces.remove(oldPiece);
        pieces.add(newPiece);
    }

    public Optional<Piece> get(Position position) {
        return pieces.stream().filter(p -> p.getPosition().equals(position)).findAny();
    }

    public Set<Piece> getPieces(Colour colour) {
        return pieces.stream().filter(p -> p.getColour().equals(colour)).collect(Collectors.toSet());
    }

    public Set<Piece> getAllPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        if (get(piece.getPosition()).isPresent()) {
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