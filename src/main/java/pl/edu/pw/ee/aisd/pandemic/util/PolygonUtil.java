package pl.edu.pw.ee.aisd.pandemic.util;

import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.point.Point;

import java.util.List;
import java.util.Optional;

public final class PolygonUtil {

    // https://stackoverflow.com/a/218081
    public static boolean isInsidePolygon(final Point point, final MapBounds mapBounds) {
        int intersections = 0;

        final List<Point> polygon = mapBounds.getCountryPolygon();
        for (int i = 0; i < polygon.size() - 1; i++) {
            final Point sideStart = polygon.get(i);
            final Point sideEnd = polygon.get(i + 1);

            final Optional<Point> intersection = VectorUtil.intersectVectors(mapBounds.getOutsidePoint(), point, sideStart, sideEnd);
            if (!intersection.isPresent()) {
                continue;
            }

            final Point intersectionPoint = intersection.get();
            if (intersectionPoint.getX() == sideStart.getX() && intersectionPoint.getY() == sideStart.getY()) {
                intersections++;
            }

            intersections++;
        }

        return intersections % 2 == 1;
    }

    private PolygonUtil() {}

}
