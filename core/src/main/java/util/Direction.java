package util;

/**
 * Representing a direction on a 2D grid.
 */
public enum Direction {
    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0), NONE(0, 0);

    private final int corX;
    private final int corY;

    Direction(int x, int y) {
        this.corX = x;
        this.corY = y;
    }

    public int getY() {
        return corY;
    }

    public int getX() {
        return corX;
    }
}
