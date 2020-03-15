package org.chris.atty.chess.piece;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.chris.atty.chess.*;

public class King extends Piece
{
    public King(Colour colour) {
        super(colour);
    }

    @Override
    public Set<Position> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = board.toArray();
        Set<Position> moves = new HashSet<>();
        if (!board.find(this).isPresent()) {
            throw new IllegalStateException("King is not on the board");
        }
        Position currentPosition = board.find(this).get();
        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();

        for (int x = currentX - 1; x <= currentX + 1; x++) {
            for (int y = currentY - 1; y <= currentY + 1; y++) {
                if ((x != currentX || y != currentY) && MoveUtils.canMoveTo(this, x, y, boardArray)) {
                    moves.add(Position.fromCoords(x, y));
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
                    Position rookPosition = board.find(rook).get();
                    for (int x = Math.min(currentX, rookPosition.getX()) + 1; x < Math.max(currentX, rookPosition.getX()); x++) {
                        if (board.get(Position.fromCoords(x, rookPosition.getY())).isPresent()) {
                            piecesBetween = true;
                        }
                    }
                    if (!piecesBetween) {
                        int xMove = rookPosition.getX() < currentX ? -2 : 2;
                        moves.add(Position.fromCoords(currentX + xMove, currentY));
                    }
                }
            });
        }
        // TODO make sure we're not moving next to another king.
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
        Position currentKingPos = board.find(this).get();
        return board.getAllPieces()
                .stream()
                .filter(p -> !p.getColour().equals(colour) && !p.getClass().equals(King.class))
                .map(p -> p.getValidMoves(board))
                .flatMap(Set::stream)
                .filter(p -> p.equals(currentKingPos))
                .findAny()
                .isPresent();
    }
}