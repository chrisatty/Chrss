package org.chris.atty.chess.piece;

import java.util.*;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.swing.border.Border;

import org.chris.atty.chess.*;

public class King extends Piece
{
    public King(Colour colour, Position position) {
        super(colour, position);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        int currentX = position.getX();
        int currentY = position.getY();
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();
        for (int x = position.getX() - 1; x < position.getX() + 1; x++) {
            for (int y = currentY - 1; y < currentY + 1; y++) {
                if (x != currentX && y != currentY && MoveUtils.canMoveTo(this, x, y, boardArray)) {
                    moves.add(new Move(this, Position.fromCoords(x, y)));
                }
            }
        }
        // can we castle?
        if (numMoves == 0 && !isInCheck(board)) {
            Set<Rook> rooks = board.getPieces(colour)
                                .stream()
                                .filter(p -> p.getClass().equals(Rook.class))
                                .map(p -> (Rook) p)
                                .collect(Collectors.toSet());
            rooks.forEach(rook -> {
                if (rook.getNumMoves() == 0) {
                    boolean piecesBetween = false;
                    for (int x = Math.min(currentX, rook.getPosition().getX()) + 1; x < Math.max(currentX, rook.getPosition().getX()); x++) {
                        if (board.get(Position.fromCoords(x, position.getY())).isPresent()) {
                            piecesBetween = true;
                        }
                    }
                    if (!piecesBetween) {
                        int xMove = rook.getPosition().getX() < currentX ? -2 : 2;
                        moves.add(new Move(this, Position.fromCoords(currentX + xMove, currentY)));
                    }
                }
            });
        }
        return moves;
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    public int getWorth() {
        return Integer.MAX_VALUE;
    }


    public boolean isInCheck(Board board) {
        return board.getAllPieces()
                .stream()
                .filter(p -> !p.getColour().equals(colour) && !p.getClass().equals(King.class))
                .map(p -> p.getValidMoves(board))
                .flatMap(Set::stream)
                .filter(m -> m.getPosition().equals(position))
                .findAny()
                .isPresent();
        // Optional<Move> piece = board.getAllPieces()
        //         .stream()
        //         .filter(p -> !p.getColour().equals(colour) && !p.getClass().equals(King.class))
        //         .map(p -> p.getValidMoves(board))
        //         .flatMap(Set::stream)
        //         .filter(m -> m.getPosition().equals(position))
        //         .findAny();
        // if (piece.isPresent()) {

        // }
    }

    @Override
    public King clone() {
        King clone = new King(colour, position);
        clone.numMoves = numMoves;
        return clone;
    }
}