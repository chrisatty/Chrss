package org.chris.atty.chess;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.chris.atty.chess.piece.*;
import org.junit.Test;

public class GameTest 
{
    @Test
    public void correctNumberOfPiecesOnBoard() {
        Game game = new Game();
        assertEquals(32, game.getBoard().size());
        Set<Piece> white = game.getBoard().values().stream().filter(p -> p.getColour().equals(Colour.WHITE)).collect(Collectors.toSet());
        Set<Piece> black = game.getBoard().values().stream().filter(p -> p.getColour().equals(Colour.BLACK)).collect(Collectors.toSet());
        assertEquals(16, black.size());
        assertEquals(16, white.size());
        assertEquals(8L, black.stream().filter(p -> p.getClass().equals(Pawn.class)).count());
        assertEquals(8L, white.stream().filter(p -> p.getClass().equals(Pawn.class)).count());
        assertEquals(2L, black.stream().filter(p -> p.getClass().equals(Knight.class)).count());
        assertEquals(2L, white.stream().filter(p -> p.getClass().equals(Knight.class)).count());
        assertEquals(2L, black.stream().filter(p -> p.getClass().equals(Bishop.class)).count());
        assertEquals(2L, white.stream().filter(p -> p.getClass().equals(Bishop.class)).count());
        assertEquals(2L, black.stream().filter(p -> p.getClass().equals(Rook.class)).count());
        assertEquals(2L, white.stream().filter(p -> p.getClass().equals(Rook.class)).count());
        assertEquals(1L, black.stream().filter(p -> p.getClass().equals(Queen.class)).count());
        assertEquals(1L, white.stream().filter(p -> p.getClass().equals(Queen.class)).count());
        assertEquals(1L, black.stream().filter(p -> p.getClass().equals(King.class)).count());
        assertEquals(1L, white.stream().filter(p -> p.getClass().equals(King.class)).count());
    }

    @Test
    public void whiteMovesFirst() {
        Game game = new Game();
        assertEquals(Colour.WHITE, game.nextMove());
    }

    @Test
    public void nextMoveColourChangesAfterMove() {
        Game game = new Game();
        game.makeMove('D', 2, 'D', 4);
        assertEquals(Colour.BLACK, game.nextMove());
    }

    @Test
    public void nextMoveColourDoesntChangeAfterInvalidMove() {
        Game game = new Game();
        game.makeMove('D', 2, 'D', 5);
        assertEquals(Colour.WHITE, game.nextMove());
    }

    @Test
    public void testCheck() {
        Game game = new Game();
        game.makeMove('d', 2, 'd', 4);
        game.makeMove('d', 7, 'd', 5);
        game.makeMove('c', 1, 'f', 4);
        game.makeMove('g', 8, 'f', 6);
        game.makeMove('e', 1, 'c', 3);
        game.makeMove('e', 8, 'd', 7);
        game.makeMove('c', 3, 'c', 7);
        assertTrue(game.inCheck());
        assertEquals(Game.State.IN_PROGRESS, game.getState());
    }

    @Test
    public void testCheckmate() {
        Game game = new Game();
        game.makeMove('d', 2, 'd', 4);
        game.makeMove('d', 7, 'd', 5);
        game.makeMove('c', 1, 'f', 4);
        game.makeMove('g', 8, 'f', 6);
        game.makeMove('e', 1, 'c', 3);
        game.makeMove('h', 7, 'h', 6);
        game.makeMove('c', 3, 'c', 7);
        assertEquals(Game.State.WHITE_WIN, game.getState());
    }

    @Test
    public void testWhiteCastle() {
        Game game = new Game();
        game.makeMove('d', 2, 'd', 4);
        game.makeMove('d', 7, 'd', 5);
        game.makeMove('c', 1, 'f', 4);
        game.makeMove('g', 8, 'f', 6);
        game.makeMove('b', 1, 'c', 3);
        game.makeMove('h', 7, 'h', 6);
        // this is the castle move so assert it was successful
        assertTrue(game.makeMove('d', 1, 'b', 1));
    }

    @Test
    public void testBlackCastle() {
        Game game = new Game();
        game.makeMove('d', 2, 'd', 4); // W
        game.makeMove('d', 7, 'd', 5); 
        game.makeMove('c', 1, 'f', 4); // W
        game.makeMove('e', 8, 'd', 7); 
        game.makeMove('b', 1, 'c', 3); // W
        game.makeMove('e', 7, 'e', 6);
        game.makeMove('h', 2, 'h', 3); // W
        game.makeMove('f', 8, 'b', 4);
        game.makeMove('h', 3, 'h', 4); // W
        game.makeMove('g', 8, 'f', 6);
        game.makeMove('h', 4, 'h', 5); // W

        // this is the castle move so assert it was successful
        assertTrue(game.makeMove('d', 8, 'f', 8));
        assertEquals(King.class, game.getBoard().get(new Position('F', 8)).getClass());
        assertEquals(Colour.BLACK, game.getBoard().get(new Position('F', 8)).getColour());
        assertEquals(Rook.class, game.getBoard().get(new Position('E', 8)).getClass());
        assertEquals(Colour.BLACK, game.getBoard().get(new Position('E', 8)).getColour());
    }
}
