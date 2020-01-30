package org.chris.atty.chess;

public class Position {

    private char file;
    private int rank;

    public Position(char file, int rank) {
        this.file = Character.toUpperCase(file);
        this.rank = rank;
    }
    public static Position fromCoords(int x, int y) {
        return new Position((char) (x + (int) 'A'), y + 1);
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public int getX() {
        return file - 'A';
    }

    public int getY() {
        return rank - 1;
    }

    @Override
    public boolean equals(Object object) {
        return object.getClass().equals(this.getClass())
            && ((Position) object).getFile() == file && ((Position) object).getRank() == rank;
    }

    @Override
    public int hashCode() {
        return 31 * (int) (rank ^ (rank >>> 32)) + (int) (file ^ file >>> 32);
    }

    @Override
    public String toString() {
        return String.valueOf(file) + rank;
    }
}