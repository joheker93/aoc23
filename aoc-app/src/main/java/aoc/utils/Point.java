package aoc.utils;

import java.util.Objects;

public class Point {
    public int x;
    public int y;
    int hash;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
        hash = Objects.hash(x,y);
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Point p) {
            return p.x == x && p.y == y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

}
