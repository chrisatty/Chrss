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

    public Board getBoard() {
        return board;
    }

    public boolean makeMove(char x, int y, char newX, int newY) {
        return makeMove(
            String.valueOf(x).toLowerCase().charAt(0) - 'a',
            y - 1,
            String.valueOf(newX).toLowerCase().charAt(0) - 'a',
            newY - 1
        );
    }

    public boolean makeMove(int oldX, int oldY, int newX, int newY) {
        Optional<Piece> piece = board.get(oldX, oldY);
        if (!piece.isPresent()) {
            System.out.println("There is no piece there to move " + oldX + oldY);
            return false;
        }
        return makeMove(new Move(piece.get(), newX, newY));
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
        if (!board.inBounds(move.getX(), move.getY())) {
            System.out.println("You cant move a piece off the board");
            return false;
        }
        // check if valid
        if (!move.getPiece().getValidMoves(board).contains(move)) {
            System.out.print("Invalid move.");
            return false;
        }
        // make sure this move doesnt put us in check
        Board updatedBoard = board.clone();
        Piece pieceToMove = updatedBoard.get(move.getPiece().getX(), move.getPiece().getY()).get();
        updatedBoard.move(pieceToMove, move.getX(), move.getY());
        if (inCheck(updatedBoard)) {
            System.out.println("You can't be in check");
            return false;
        }        

        // are we castling?
        boolean castle = move.getPiece().getClass().equals(King.class)
            && Math.abs(move.getPiece().getX()) -  Math.abs(move.getX()) == 2;

        Optional<Piece> removedPiece = board.move(move.getPiece(), move.getX(), move.getY());
        if (removedPiece.isPresent()) {
            removedPieces.add(removedPiece.get());
        }

        if (castle) {
            int rookX = move.getPiece().getX() == 1 ? 0 : board.WIDTH - 1;
            Piece rook = board.get(rookX, move.getPiece().getY()).get();
            int rookNewX = move.getPiece().getX() == 1 ? 2 : 4;
            board.move(rook, rookNewX, move.getPiece().getY());
        }

        history.add(move);

        // see if any pawns need to be converted to Queens
        Optional<Piece> convertPawn = board.getAllPieces()
                            .stream()
                            .filter(p -> p.getClass().equals(Pawn.class))
                            .filter(p -> p.getY() == 0 || p.getY() == board.HEIGHT -1)
                            .findAny();
        if (convertPawn.isPresent()) {
            Piece pawn = convertPawn.get();
            board.replace(pawn, new Queen(pawn.getColour(), pawn.getX(), pawn.getY()));
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
            Piece pieceToMove = newBoard.get(move.getPiece().getX(), move.getPiece().getY()).get();
            newBoard.move(pieceToMove, move.getX(), move.getY());
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
        IntStream.range(0, board.WIDTH).forEach(x -> board.addPiece(new Pawn(Colour.WHITE, x, 1)));
        board.addPiece(new Rook(Colour.WHITE, 0, 0));
        board.addPiece(new Knight(Colour.WHITE, 1, 0));
        board.addPiece(new Bishop(Colour.WHITE, 2, 0));
        board.addPiece(new King(Colour.WHITE, 3, 0));
        board.addPiece(new Queen(Colour.WHITE, 4, 0));
        board.addPiece(new Bishop(Colour.WHITE, 5, 0));
        board.addPiece(new Knight(Colour.WHITE, 6, 0));
        board.addPiece(new Rook(Colour.WHITE, 7, 0));

        // black
        IntStream.range(0, board.WIDTH).forEach(x -> board.addPiece(new Pawn(Colour.BLACK, x, 6)));
        board.addPiece(new Rook(Colour.BLACK, 0, 7));
        board.addPiece(new Knight(Colour.BLACK, 1, 7));
        board.addPiece(new Bishop(Colour.BLACK, 2, 7));
        board.addPiece(new King(Colour.BLACK, 3, 7));
        board.addPiece(new Queen(Colour.BLACK, 4, 7));
        board.addPiece(new Bishop(Colour.BLACK, 5, 7));
        board.addPiece(new Knight(Colour.BLACK, 6, 7));
        board.addPiece(new Rook(Colour.BLACK, 7, 7));
        return board;
    }
}