package org.chris.atty.chess;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            if (this.makeMove(move)) {
                throw new IllegalArgumentException("Invalid move");
            }
        });
    }

    public boolean makeMove(char oldF, int oldR, char newF, int newR) {
        return makeMove(new Position(oldF, oldR), new Position(newF, newR));
    }

    public boolean makeMove(Position oldPos, Position newPos) {
        Optional<Piece> piece = board.get(oldPos);
        if (!piece.isPresent()) {
            System.out.println("There is no piece there to move " + oldPos);
            return false;
        }
        return makeMove(new Move(piece.get(), newPos));
    }

    public boolean makeMove(Move move) {
        if (!getState().equals(State.IN_PROGRESS)) {
            System.out.println("Game has finished");
            return false;
        }
        if (!move.getPiece().getColour().equals(nextMove())) {
            System.out.println("Wrong colour");
            return false;
        }
        if (!board.inBounds(move.getPosition())) {
            System.out.println("You cant move a piece off the board");
            return false;
        }
        // check if valid
        if (!move.getPiece().getValidMoves(board).contains(move)) {
            System.out.println("Invalid move " + move);
            return false;
        }
        // make sure this move doesnt put us in check
        Board updatedBoard = board.clone();
        Piece pieceToMove = updatedBoard.get(move.getPiece().getPosition()).get();
        updatedBoard.move(pieceToMove, move.getPosition());
        if (inCheck(updatedBoard)) {
            System.out.println("You can't be in check");
            return false;
        }

        Optional<Piece> removedPiece = board.move(move.getPiece(), move.getPosition());
        if (removedPiece.isPresent()) {
            removedPieces.add(removedPiece.get());
        }
        
        if (move.isCastle()) {
            System.out.println("We've castled");
            Optional<Piece> piece = board.get(new Position(
                move.getPosition().getFile() == 'B' ? 'A' : 'H', move.getPosition().getRank())
            );
            if (!piece.isPresent() || !piece.get().getClass().equals(Rook.class)
                    || !piece.get().getColour().equals(move.getPiece().getColour())) {
                throw new IllegalStateException("Rook not in correct place to castle");
            }
            Rook rook = (Rook) piece.get();
            board.move(rook, new Position(
                move.getPosition().getFile() == 'B' ? 'C' : 'E',
                move.getPosition().getRank())
            );
        }

        history.add(move);

        if (move.isPawnPromote()) {
            board.replace(move.getPiece(), new Queen(move.getPiece().getColour(), move.getPiece().getPosition()));
        }

        if (isCheckmate()) {
            state = nextMove().equals(Colour.WHITE) ? State.BLACK_WIN : State.WHITE_WIN;
        }
        return true;
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
        Set<Move> validMoves = board.getPieces(nextMove())
                    .stream()
                    .map(p -> p.getValidMoves(board))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        // if no valid moves can get out of check, its checkmate
        for (Move move: validMoves) {
            Board newBoard = board.clone();
            // we need to move the cloned piece, so retrieve it from new board
            Piece pieceToMove = newBoard.get(move.getPiece().getPosition()).get();
            newBoard.move(pieceToMove, move.getPosition());
            if (!inCheck(newBoard)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Optional<Piece>[][] boardArray = Utils.toArray(board);
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
        builder.append("     A   B   C   D   E   F   G   H    ");
        builder.append(System.lineSeparator());
        for (int y = board.HEIGHT - 1; y >= 0 ; y--) {
            builder.append("   ---------------------------------   ");
            builder.append(System.lineSeparator());
            builder.append(" " + (y+1) + " ");
            for (int x = 0; x < board.WIDTH; x++) {
                Optional<Piece> piece = boardArray[x][y];
                if (piece.isPresent()) {
                    builder.append("| " + piece.get().icon());
                } else {
                    builder.append("|   ");
                }
                
            }
            builder.append("|");
            builder.append(" " + (y+1) + " ");
            builder.append(System.lineSeparator());
        }
        builder.append("   ---------------------------------   ");
        builder.append(System.lineSeparator());
        builder.append("     A   B   C   D   E   F   G   H    ");
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
            board.addPiece(new Pawn(Colour.WHITE, new Position(rank, 2)));
        }
        board.addPiece(new Rook(Colour.WHITE, new Position('A', 1)));
        board.addPiece(new Knight(Colour.WHITE, new Position('B', 1)));
        board.addPiece(new Bishop(Colour.WHITE, new Position('C', 1)));
        board.addPiece(new King(Colour.WHITE, new Position('D', 1)));
        board.addPiece(new Queen(Colour.WHITE, new Position('E', 1)));
        board.addPiece(new Bishop(Colour.WHITE, new Position('F', 1)));
        board.addPiece(new Knight(Colour.WHITE, new Position('G', 1)));
        board.addPiece(new Rook(Colour.WHITE, new Position('H', 1)));

        // black
        for(char rank = 'A'; rank <= 'H'; rank++ ) {
            board.addPiece(new Pawn(Colour.BLACK, new Position(rank, 7)));
        }
        board.addPiece(new Rook(Colour.BLACK, new Position('A', 8)));
        board.addPiece(new Knight(Colour.BLACK, new Position('B', 8)));
        board.addPiece(new Bishop(Colour.BLACK, new Position('C', 8)));
        board.addPiece(new King(Colour.BLACK, new Position('D', 8)));
        board.addPiece(new Queen(Colour.BLACK, new Position('E', 8)));
        board.addPiece(new Bishop(Colour.BLACK, new Position('F', 8)));
        board.addPiece(new Knight(Colour.BLACK, new Position('G', 8)));
        board.addPiece(new Rook(Colour.BLACK, new Position('H', 8)));
        return board;
    }

    public Map<Position, Piece> getBoard() {
        Map<Position, Piece> map = new HashMap<>();
        board.getAllPieces().forEach(p -> {
            map.put(p.getPosition(), p);
        });
        return map;
    }
}