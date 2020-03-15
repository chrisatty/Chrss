package org.chris.atty.chess;

public class Move {

    private final Position oldPosition;
    private final Position newPosition;

    public Move(Position oldPosition, Position newPosition) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    public Position getOldPosition() {
        return oldPosition;
    }

    public Position getNewPosition() {
        return newPosition;
    }
}