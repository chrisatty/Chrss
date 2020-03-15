package org.chris.atty.chess;

import java.util.*;
import java.util.stream.Collectors;

import org.chris.atty.chess.exceptions.InvalidMoveException;
import org.chris.atty.chess.piece.*;

public class Game
{
    public enum State { IN_PROGRESS, WHITE_WIN, BLACK_WIN };

    private final List<Move> history = new ArrayList<>();
    private final Board board;
    private final Set<Piece> removedPieces = new HashSet<>();
    private State state = State.IN_PROGRESS;

    public Game() {
        board = createBoard();
    }

    public Game(List<Move> moves) {
        this();
        moves.forEach(move -> {
            try {
                this.makeMove(move.getOldPosition(), move.getNewPosition());
            } catch (InvalidMoveException e) {
                throw new IllegalArgumentException("Invalid move");
            }
        });
    }

    public Board makeMove(char oldF, int oldR, char newF, int newR) throws InvalidMoveException {
        return makeMove(new Position(oldF, oldR), new Position(newF, newR));
    }

    public Board makeMove(Position oldPos, Position newPos) throws InvalidMoveException {
        // check is valid move
        if (!getState().equals(State.IN_PROGRESS)) {
            throw new InvalidMoveException("Game is over");
        }
        if (!board.inBounds(oldPos) || !board.inBounds(newPos)) {
            throw new InvalidMoveException("Invalid board position");
        }
        if (!board.get(oldPos).isPresent()) {
            throw new InvalidMoveException("There is no piece at that position");
        }

        Piece pieceToMove = board.get(oldPos).get();
        if (!pieceToMove.getColour().equals(nextMove())) {
            throw new InvalidMoveException("Incorrect coloured piece to move");
        }
        if (!pieceToMove.getValidMoves(board).contains(newPos)) {
            throw new InvalidMoveException("Invalid move");
        }
        // make sure this move doesnt put us in check
        Board updatedBoard = board.clone();
        updatedBoard.move(oldPos, newPos);
        if (inCheck(updatedBoard)) {
            throw new InvalidMoveException("You cannot move into check");
        }

        Optional<Piece> removedPiece = board.move(oldPos, newPos);
        pieceToMove.incMoveCount();
        
        if (removedPiece.isPresent()) {
            removedPieces.add(removedPiece.get());
        }
        
        // check if we've castled and move rook if so
        if (pieceToMove.getClass().equals(King.class) && Math.abs(oldPos.getFile() - newPos.getFile()) >= 2) {
            Position rookCurrent = newPos.getFile() == 'G' ? new Position('H', newPos.getRank()) : new Position('A', newPos.getRank());
            Position rookNew = newPos.getFile() == 'G' ? new Position('F', newPos.getRank()) : new Position('D', newPos.getRank());
            board.move(rookCurrent, rookNew);
        }

        // do we need to promote a pawn?
        if ((newPos.getRank() == 1 || newPos.getFile() == 8) && board.get(newPos).get().getClass().equals(Pawn.class)) {
            board.add(new Queen(nextMove()), newPos);
        }

        history.add(new Move(oldPos, newPos));

        if (isCheckmate()) {
            state = nextMove().equals(Colour.WHITE) ? State.BLACK_WIN : State.WHITE_WIN;
        }
        return board;
    }

    public boolean inCheck() {
        return inCheck(board);
    }

    private boolean inCheck(Board board) {
        // if any of the valid piece moves are to King's square - we're in check
        King king = (King) board.getPieces(nextMove())
                            .stream()
                            .filter(p -> p.getClass().equals(King.class))
                            .findAny()
                            .get();
        return king.isInCheck(board);   
    }


    public Colour nextMove() {
        return history.size() % 2 == 0 ? Colour.WHITE : Colour.BLACK;
    }

    public State getState() {
        return state;
    }

    private boolean isCheckmate() {
        if (!inCheck()) {
            return false;
        }
        // if no valid moves can get out of check, its checkmate
        for (Piece piece : board.getPieces(nextMove())) {
            Set<Position> validMoves = piece.getValidMoves(board);
            for (Position move : validMoves) {
                Board newBoard = board.clone();
                newBoard.move(newBoard.find(piece).get(), move);
                if (!inCheck(newBoard)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        System.out.println();
        builder.append("Captured pieces: " +
            String.join(" ", removedPieces.stream()
                .filter(p -> p.getColour().equals(Colour.WHITE))
                .sorted((p1, p2) -> p1.getWorth() < p2.getWorth() ? 1 : -1)
                .map(Piece::icon)
                .collect(Collectors.toList())
            )
        );
        builder.append(System.lineSeparator());
        builder.append(board.toString());
        builder.append(System.lineSeparator());
        builder.append("Captured pieces: " +
            String.join(" ", removedPieces.stream()
                .filter(p -> p.getColour().equals(Colour.BLACK))
                .sorted((p1, p2) -> p1.getWorth() < p2.getWorth() ? 1 : -1)
                .map(Piece::icon)
                .collect(Collectors.toList())
            )
        );
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private Board createBoard() {
        Board board = new Board();
        //white
        for(char rank = 'A'; rank <= 'H'; rank++ ) {
            board.add(new Pawn(Colour.WHITE), new Position(rank, 2));
        }
        board.add(new Rook(Colour.WHITE), new Position('A', 1));
        board.add(new Knight(Colour.WHITE), new Position('B', 1));
        board.add(new Bishop(Colour.WHITE), new Position('C', 1));
        board.add(new Queen(Colour.WHITE), new Position('D', 1));
        board.add(new King(Colour.WHITE), new Position('E', 1));
        board.add(new Bishop(Colour.WHITE), new Position('F', 1));
        board.add(new Knight(Colour.WHITE), new Position('G', 1));
        board.add(new Rook(Colour.WHITE), new Position('H', 1));

        // black
        for(char rank = 'A'; rank <= 'H'; rank++ ) {
            board.add(new Pawn(Colour.BLACK), new Position(rank, 7));
        }
        board.add(new Rook(Colour.BLACK), new Position('A', 8));
        board.add(new Knight(Colour.BLACK), new Position('B', 8));
        board.add(new Bishop(Colour.BLACK), new Position('C', 8));
        board.add(new Queen(Colour.BLACK), new Position('D', 8));
        board.add(new King(Colour.BLACK), new Position('E', 8));
        board.add(new Bishop(Colour.BLACK), new Position('F', 8));
        board.add(new Knight(Colour.BLACK), new Position('G', 8));
        board.add(new Rook(Colour.BLACK), new Position('H', 8));
        return board;
    }

    public Board getBoard() {
        return board;
    }
}