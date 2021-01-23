package pl.edu.pw.ee.aisd.pandemic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.util.VectorUtil;

import java.util.Optional;

public class VectorTester {

    @Test
    public void testVectorIntersect() {
        final Point a = Point.of(-2, -2);
        final Point b = Point.of(2, 2);
        final Point c = Point.of(2, -2);
        final Point d = Point.of(-2, 2);

        final Optional<Point> intersection = VectorUtil.intersectVectors(a, b, c, d);

        Assertions.assertTrue(intersection.isPresent());
        Assertions.assertEquals(0.0D, Math.abs(intersection.get().getX()));
        Assertions.assertEquals(0.0D, Math.abs(intersection.get().getY()));
    }

}
