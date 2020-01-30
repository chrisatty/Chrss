package org.chris.atty.chess.piece;

import java.util.*;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.swing.border.Border;

import org.chris.atty.chess.*;

public class King extends Piece
{
    private boolean firstMove = true;

    public King(Colour colour, int x, int y) {
        super(colour, x, y);
    }

    @Override
    public Set<Move> getValidMoves(Board board) {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
        Set<Move> moves = new HashSet<>();
        for (int x = currentX - 1; x < currentX + 1; x++) {
            System.out.print("-");
            for (int y = currentY - 1; y < currentY + 1; y++) {
                if (x != currentX && y != currentY && MoveUtils.canMoveTo(this, x, y, boardArray)) {
                    moves.add(new Move(this, x, y));
                }
            }
        }
        // can we castle?
        if (firstMove && !isInCheck(board)) {
            Set<Rook> rooks = board.getPieces(colour)
                                .stream()
                                .filter(p -> p.getClass().equals(Rook.class))
                                .map(p -> (Rook) p)
                                .collect(Collectors.toSet());
            rooks.forEach(rook -> {
                if (rook.canCastle()) {
                    boolean piecesBetween = false;
                    for (int x = Math.min(currentX, rook.getX()) + 1; x < Math.max(currentX, rook.getX()); x++) {
                        if (board.get(x, currentY).isPresent()) {
                            piecesBetween = true;
                        }
                    }
                    if (!piecesBetween) {
                        int xMove = rook.getX() < currentX ? -2 : 2;
                        moves.add(new Move(this, currentX + xMove, currentY));
                    }
                }
            });
        }
        return moves;
    }

    @Override
    public void move(int x, int y) {
        firstMove = false;
        super.move(x, y);
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    public int getWorth() {
        return Integer.MAX_VALUE;
    }

    public boolean canCastle() {
        return firstMove;
    }

    public boolean isInCheck(Board board) {
        return board.getAllPieces()
                .stream()
                .filter(p -> !p.getColour().equals(colour) && !p.getClass().equals(King.class))
                .map(p -> p.getValidMoves(board))
                .flatMap(Set::stream)
                .filter(m -> m.getX() == this.getX() && m.getY() == this.getY())
                .findAny()
                .isPresent();
    }

    @Override
    public King clone() {
        King king = new King(colour, currentX, currentY);
        king.firstMove = this.firstMove;
        return king;
    }
}