package org.chris.atty.chess;

import static org.junit.Assert.*;

import java.sql.SQLOutput;
import java.util.Set;

import org.chris.atty.chess.exceptions.InvalidMoveException;
import org.chris.atty.chess.piece.*;
import org.junit.Test;

public class GameTest 
{
    @Test
    public void correctNumberOfPiecesOnBoard() {
        Game game = new Game();
        assertEquals(32, game.getBoard().getAllPieces().size());
        Set<Piece> white = game.getBoard().getPieces(Colour.WHITE);
        Set<Piece> black = game.getBoard().getPieces(Colour.BLACK);;
        assertEquals(16, black.size());
        assertEquals(16, white.size());
        assertEquals(8, black.stream().filter(p -> p.getClass().equals(Pawn.class)).count());
        assertEquals(8, white.stream().filter(p -> p.getClass().equals(Pawn.class)).count());
        assertEquals(2, black.stream().filter(p -> p.getClass().equals(Knight.class)).count());
        assertEquals(2, white.stream().filter(p -> p.getClass().equals(Knight.class)).count());
        assertEquals(2, black.stream().filter(p -> p.getClass().equals(Bishop.class)).count());
        assertEquals(2, white.stream().filter(p -> p.getClass().equals(Bishop.class)).count());
        assertEquals(2, black.stream().filter(p -> p.getClass().equals(Rook.class)).count());
        assertEquals(2, white.stream().filter(p -> p.getClass().equals(Rook.class)).count());
        assertEquals(1, black.stream().filter(p -> p.getClass().equals(Queen.class)).count());
        assertEquals(1, white.stream().filter(p -> p.getClass().equals(Queen.class)).count());
        assertEquals(1, black.stream().filter(p -> p.getClass().equals(King.class)).count());
        assertEquals(1, white.stream().filter(p -> p.getClass().equals(King.class)).count());
    }

    @Test
    public void whiteMovesFirst() {
        Game game = new Game();
        assertEquals(Colour.WHITE, game.nextMove());
    }

    @Test
    public void nextMoveColourChangesAfterMove() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 4);
        assertEquals(Colour.BLACK, game.nextMove());
    }

    @Test(expected=InvalidMoveException.class)
    public void testInvalidMove() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 5);
    }

    @Test
    public void testValidMoveAfterInvalidMove() throws InvalidMoveException {
        Game game = new Game();
        try {
            game.makeMove('D', 2, 'D', 5);
        } catch (InvalidMoveException e) {

        }
        assertEquals(Colour.WHITE, game.nextMove());
        game.makeMove('D', 2, 'D', 4);
        assertEquals(Colour.BLACK, game.nextMove());
    }

    @Test
    public void testCheck() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 4);
        game.makeMove('E', 7, 'E', 5);
        game.makeMove('F', 1, 'C', 4);
        game.makeMove('B', 8, 'C', 6);
        game.makeMove('D', 1, 'F', 3);
        game.makeMove('D', 8, 'E', 7);
        game.makeMove('F', 3, 'F', 7);
        assertTrue(game.inCheck());
        assertEquals(Game.State.IN_PROGRESS, game.getState());
    }

    @Test
    public void testCheckmate() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 4);
        game.makeMove('E', 7, 'E', 5);
        game.makeMove('F', 1, 'C', 4);
        game.makeMove('B', 8, 'C', 6);
        game.makeMove('D', 1, 'F', 3);
        game.makeMove('A', 7, 'A', 6);
        game.makeMove('F', 3, 'F', 7);
        assertEquals(Game.State.WHITE_WIN, game.getState());
    }

    @Test
    public void testWhiteCastle() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 4);
        game.makeMove('E', 7, 'E', 5);
        game.makeMove('F', 1, 'C', 4);
        game.makeMove('G', 8, 'F', 6);
        game.makeMove('G', 1, 'F', 3);
        game.makeMove('H', 7, 'H', 6);
        // this is the castle move so assert it was successful
        Board board = game.makeMove('E', 1, 'G', 1);
        assertEquals(King.class, board.get(new Position('G', 1)).get().getClass());
        assertEquals(Colour.WHITE, board.get(new Position('G', 1)).get().getColour());
        assertEquals(Rook.class, board.get(new Position('F', 1)).get().getClass());
        assertEquals(Colour.WHITE, board.get(new Position('F', 1)).get().getColour());
    }

    @Test
    public void testBlackCastle() throws InvalidMoveException {
        Game game = new Game();
        game.makeMove('E', 2, 'E', 4); // W
        game.makeMove('E', 7, 'E', 5); 
        game.makeMove('F', 1, 'C', 4); // W
        game.makeMove('D', 8, 'E', 7); 
        game.makeMove('B', 1, 'C', 3); // W
        game.makeMove('D', 7, 'D', 6);
        game.makeMove('H', 2, 'H', 3); // W
        game.makeMove('C', 8, 'G', 4);
        game.makeMove('H', 3, 'H', 4); // W
        game.makeMove('B', 8, 'C', 6);
        game.makeMove('H', 4, 'H', 5); // W
        
        // this is the castle move so assert it was successful
        Board board = game.makeMove('E', 8, 'C', 8);
        assertEquals(King.class, board.get(new Position('C', 8)).get().getClass());
        assertEquals(Colour.BLACK, board.get(new Position('C', 8)).get().getColour());
        assertEquals(Rook.class, board.get(new Position('D', 8)).get().getClass());
        assertEquals(Colour.BLACK, board.get(new Position('D', 8)).get().getColour());
    }
}
