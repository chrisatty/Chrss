package org.chris.atty.chess;

import static org.junit.Assert.*;

import java.util.Set;

import org.chris.atty.chess.piece.*;
import org.junit.Test;

public class GameTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void correctNumberOfPiecesOnBoard() {
        Game game = new Game();
        assertEquals(32, game.getBoard().getAllPieces().size());
        Set<Piece> white = game.getBoard().getPieces(Colour.WHITE);
        Set<Piece> black = game.getBoard().getPieces(Colour.BLACK);
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
        game.makeMove('d', 2, 'd', 4);
        assertEquals(Colour.BLACK, game.nextMove());
    }

    @Test
    public void nextMoveColourDoesntChangeAfterInvalidMove() {
        Game game = new Game();
        game.makeMove('d', 2, 'd', 5);
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
}
