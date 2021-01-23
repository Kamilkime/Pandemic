package pl.edu.pw.ee.aisd.pandemic.util;

import pl.edu.pw.ee.aisd.pandemic.point.Point;

import java.util.Optional;

public final class VectorUtil {

    // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line
    public static Optional<Point> intersectVectors(final Point p1, final Point p2, final Point p3, final Point p4) {
        final double[] lines = VectorUtil.checkPosition(p1, p2, p3, p4);
        if (lines.length == 0) {
            return Optional.empty();
        }

        final double denominator = lines[0] * lines[4] - lines[1] * lines[3];
        if (Math.abs(denominator) < 0.000001D) {
            return Optional.empty();
        }

        final double x = (lines[1] * lines[5] - lines[2] * lines[4]) / denominator;
        final double y = (lines[2] * lines[3] - lines[0] * lines[5]) / denominator;

        return Optional.of(Point.of(x, y));
    }

    // https://stackoverflow.com/a/218081
    private static double[] checkPosition(final Point p1, final Point p2, final Point p3, final Point p4) {
        final double a1 = p2.getY() - p1.getY();
        final double b1 = p1.getX() - p2.getX();
        final double c1 = (p2.getX() * p1.getY()) - (p1.getX() * p2.getY());

        double d1 = (a1 * p3.getX()) + (b1 * p3.getY()) + c1;
        double d2 = (a1 * p4.getX()) + (b1 * p4.getY()) + c1;

        if ((d1 > 0.0D && d2 > 0.0D) || (d1 < 0.0D && d2 < 0.0D)) {
            return new double[0];
        }

        final double a2 = p4.getY() - p3.getY();
        final double b2 = p3.getX() - p4.getX();
        final double c2 = (p4.getX() * p3.getY()) - (p3.getX() * p4.getY());

        d1 = (a2 * p1.getX()) + (b2 * p1.getY()) + c2;
        d2 = (a2 * p2.getX()) + (b2 * p2.getY()) + c2;

        if ((d1 > 0.0D && d2 > 0.0D) || (d1 < 0.0D && d2 < 0.0D)) {
            return new double[0];
        }

        if (Math.abs((a1 * b2) - (a2 * b1)) < 0.000001D) {
            return new double[0];
        }

        return new double[]{a1, b1, c1, a2, b2, c2};
    }

    private VectorUtil() {}

}
