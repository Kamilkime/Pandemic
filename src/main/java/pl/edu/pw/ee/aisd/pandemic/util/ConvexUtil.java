package pl.edu.pw.ee.aisd.pandemic.util;

import pl.edu.pw.ee.aisd.pandemic.point.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public final class ConvexUtil {

    // https://en.wikipedia.org/wiki/Graham_scan
    public static List<Point> createHull(final List<Point> points) {
        if (points.size() < 3 || areCollinear(points)) {
            return new ArrayList<>();
        }

        points.sort(Comparator.comparingDouble(Point::getY).thenComparingDouble(Point::getX));
        final Point startPoint = points.get(0);

        points.subList(1, points.size()).sort((p1, p2) -> {
            final double dx = p1.getX() - p2.getX();
            final double dy = p1.getY() - p2.getY();

            if (Math.abs(dx) < 0.000001D && Math.abs(dy) < 0.000001D) {
                return 0;
            }

            final Turn orientation = Turn.getTurn(startPoint, p1, p2);
            if (orientation == Turn.COLLINEAR) {
                return Double.compare(p1.distance(startPoint), p2.distance(startPoint));
            }

            return orientation == Turn.COUNTER_CLOCKWISE ? -1 : 1;
        });

        final Deque<Point> hull = new ArrayDeque<>();
        hull.push(points.get(0));
        hull.push(points.get(1));
        hull.push(points.get(2));

        for (int i = 3; i < points.size(); i++) {
            final Point a = points.get(i);
            Point b;
            Point c;

            do {
                b = hull.pop();
                c = hull.peek();

                if (c == null) {
                    return new ArrayList<>();
                }
            } while (Turn.getTurn(c, b, a) != Turn.COUNTER_CLOCKWISE);

            hull.push(b);
            hull.push(a);
        }

        final List<Point> polygon = new ArrayList<>(hull);
        polygon.add(polygon.get(0));
        return polygon;
    }

    private static boolean areCollinear(final List<Point> points) {
        if (points.size() < 3) {
            return true;
        }

        final Point a = points.get(0);
        final Point b = points.get(1);

        for (int i = 2; i < points.size(); i++) {
            final Point c = points.get(i);
            if (Turn.getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    private enum Turn {
        CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR;

        public static Turn getTurn(final Point a, final Point b, final Point c) {
            final double crossProduct = (b.getY() - a.getY()) * (c.getX() - b.getX()) - (b.getX() - a.getX()) * (c.getY() - b.getY());
            return Math.abs(crossProduct) < 0.000001D ? COLLINEAR : (crossProduct > 0 ? CLOCKWISE : COUNTER_CLOCKWISE);
        }
    }

    private ConvexUtil() {}

}
