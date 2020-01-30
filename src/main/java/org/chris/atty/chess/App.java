package org.chris.atty.chess;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        while (game.getState().equals(Game.State.IN_PROGRESS)) {
            System.out.println(game);
            System.out.print("Enter the piece to move: ");
            String piece = scanner.nextLine();
            if (piece.length() != 2) {
                System.out.print("Invalid input");
                continue;
            }
            char x = piece.charAt(0);
            int y = Integer.valueOf(String.valueOf(piece.charAt(1)));
            System.out.print("Where you want to move it to? ");
            String move = scanner.nextLine();
            if (move.length() != 2) {
                System.out.println("Invalid input");
                continue;
            }
            char newX = move.charAt(0);
            int newY = Integer.valueOf(String.valueOf(move.charAt(1)));
            System.out.println("Moving " + x + y + " to " + newX + newY);
            if (!game.makeMove(new Position(x, y), new Position(newX, newY))) {
                System.out.println("Invalid move");
            }
            if (game.inCheck() && game.getState().equals(Game.State.IN_PROGRESS)) {
                System.out.println("Careful, you are in check");
            }
        }
        System.out.println(game);
        System.out.println(game.getState());
        scanner.close();
    }
}