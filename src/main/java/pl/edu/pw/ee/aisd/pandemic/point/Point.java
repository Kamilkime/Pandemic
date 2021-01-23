package pl.edu.pw.ee.aisd.pandemic.point;

public class Point {

    private final double x;
    private final double y;

    protected Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getPositionString() {
        return "x = " + this.x + ", y = " + this.y;
    }

    public static Point of(final double x, final double y) {
        return new Point(x, y);
    }

    public double distanceSquared(final Point point) {
        return Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2);
    }

    public double distance(final Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }

}
