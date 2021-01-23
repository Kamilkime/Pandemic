package pl.edu.pw.ee.aisd.pandemic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.util.PolygonUtil;

import java.util.Arrays;

public class PolygonTester {

    @Test
    public void testPoints() {
        final Point a = Point.of(-7, 8);
        final Point b = Point.of(-4, 6);
        final Point c = Point.of(2, 6);
        final Point d = Point.of(6, 4);
        final Point e = Point.of(8, 6);
        final Point f = Point.of(7, -2);
        final Point g = Point.of(4, -6);
        final Point h = Point.of(8, -7);
        final Point i = Point.of(0, 0);
        final Point j = Point.of(3, -2);
        final Point k = Point.of(6, -10);
        final Point l = Point.of(0, -6);
        final Point m = Point.of(-9, -5);
        final Point n = Point.of(-8, -2);
        final Point o = Point.of(-8, 0);
        final Point p = Point.of(-10, 3);
        final Point q = Point.of(-3, 3);
        final Point r = Point.of(-10, 4);

        final MapBounds mapBounds = new MapBounds(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));

        Assertions.assertTrue(PolygonUtil.isInsidePolygon(Point.of(-2, 2), mapBounds));
        Assertions.assertTrue(PolygonUtil.isInsidePolygon(Point.of(4.71, 6.29), mapBounds));
        Assertions.assertFalse(PolygonUtil.isInsidePolygon(Point.of(6, 10), mapBounds));
        Assertions.assertFalse(PolygonUtil.isInsidePolygon(Point.of(-1.45, 7.46), mapBounds));
    }

    @Test
    public void testRayThroughVertex() {
        final Point a = Point.of(-2, -2);
        final Point b = Point.of(2, 2);
        final Point c = Point.of(2, -2);
        final Point d = Point.of(-2, 2);

        Assertions.assertTrue(PolygonUtil.isInsidePolygon(Point.of(0, 0), new MapBounds(Arrays.asList(a, b, c, d))));
    }

}
