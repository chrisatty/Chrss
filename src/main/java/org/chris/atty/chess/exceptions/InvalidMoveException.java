package org.chris.atty.chess.exceptions;

public class InvalidMoveException extends Exception {

    public InvalidMoveException(String reason) {
        super(reason);
    }
}