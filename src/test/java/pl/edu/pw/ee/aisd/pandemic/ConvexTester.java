package pl.edu.pw.ee.aisd.pandemic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.monument.Monument;
import pl.edu.pw.ee.aisd.pandemic.point.Point;

import java.util.Arrays;
import java.util.Collections;

public class ConvexTester {

    @Test
    public void testConvexHull() {
        final Point a = new Monument(0, -7, 8, "a");
        final Point b = new Monument(0, -4, 6, "b");
        final Point c = new Monument(0, 2, 6, "c");
        final Point d = new Monument(0, 6, 4, "d");
        final Point e = new Monument(0, 8, 6, "e");
        final Point f = new Monument(0, 7, -2, "f");
        final Point g = new Monument(0, 4, -6, "g");
        final Point h = new Monument(0, 8, -7, "h");
        final Point i = new Monument(0, 0, 0, "i");
        final Point j = new Monument(0, 3, -2, "j");
        final Point k = new Monument(0, 6, -10, "k");
        final Point l = new Monument(0, 0, -6, "l");
        final Point m = new Monument(0, -9, -5, "m");
        final Point n = new Monument(0, -8, -2, "n");
        final Point o = new Monument(0, -8, 0, "o");
        final Point p = new Monument(0, -10, 3, "p");
        final Point q = new Monument(0, -3, 3, "q");
        final Point r = new Monument(0, -10, 4, "r");

        final MapBounds mapBounds = new MapBounds(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
        Assertions.assertIterableEquals(mapBounds.getCountryPolygon(), Arrays.asList(m, p, r, a, e, h, k, m));
    }

    @Test
    public void testOnePoint() {
        final MapBounds mapBounds = new MapBounds(Collections.singletonList(new Monument(-7, 8, 0, "a")));
        Assertions.assertNull(mapBounds.getOutsidePoint());
    }

    @Test
    public void testCollinearPoints() {
        final Point a = new Monument(0, 1, 0, "a");
        final Point b = new Monument(0, 2, 1, "b");
        final Point c = new Monument(0, 3, 2, "c");
        final Point d = new Monument(0, 4, 3, "d");
        final Point e = new Monument(0, 5, 4, "e");

        final MapBounds mapBounds = new MapBounds(Arrays.asList(a, b, c, d, e));
        Assertions.assertNull(mapBounds.getOutsidePoint());
    }

}
