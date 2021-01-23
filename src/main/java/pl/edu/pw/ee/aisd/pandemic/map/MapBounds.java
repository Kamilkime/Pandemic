package pl.edu.pw.ee.aisd.pandemic.map;

import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.util.ConvexUtil;

import java.util.Arrays;
import java.util.List;

public class MapBounds {

    public static final int PANE_RIGHT_X = 799;
    public static final int PANE_BOTTOM_Y = 699;

    private final double ax;
    private final double bx;
    private final double ay;
    private final double by;

    private final Point outsidePoint;
    private final List<Point> countryPolygon;

    public MapBounds(final List<Point> mapPoints) {
        this.countryPolygon = ConvexUtil.createHull(mapPoints);

        if (this.countryPolygon.isEmpty()) {
            this.outsidePoint = null;

            this.ax = 0.0D;
            this.bx = 0.0D;
            this.ay = 0.0D;
            this.by = 0.0D;

            return;
        }

        final double[] countryBounds = new double[]{Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        for (final Point point : this.countryPolygon) {
            countryBounds[0] = Math.max(countryBounds[0], point.getY());
            countryBounds[1] = Math.max(countryBounds[1], point.getX());
            countryBounds[2] = Math.min(countryBounds[2], point.getY());
            countryBounds[3] = Math.min(countryBounds[3], point.getX());
        }

        final double vRange = countryBounds[0] - countryBounds[2];
        final double hRange = countryBounds[1] - countryBounds[3];

        final double[] mapBounds = Arrays.copyOf(countryBounds, countryBounds.length);

        mapBounds[0] += 0.1D * vRange;
        mapBounds[1] += 0.1D * hRange;
        mapBounds[2] -= 0.1D * vRange;
        mapBounds[3] -= 0.1D * hRange;

        this.outsidePoint = Point.of(mapBounds[3], mapBounds[0]);

        this.ax = PANE_RIGHT_X / (mapBounds[1] - mapBounds[3]);
        this.bx = -1.0D * this.ax * mapBounds[3];

        this.ay = PANE_BOTTOM_Y / (mapBounds[2] - mapBounds[0]);
        this.by = -1.0D * this.ay * mapBounds[0];
    }

    public List<Point> getCountryPolygon() {
        return this.countryPolygon;
    }

    public Point getOutsidePoint() {
        return this.outsidePoint;
    }

    public Point normalizeForPane(final Point point) {
        final double finalX = Math.max(0.0D, Math.min(PANE_RIGHT_X, this.ax * point.getX() + this.bx));
        final double finalY = Math.max(0.0D, Math.min(PANE_BOTTOM_Y, this.ay * point.getY() + this.by));

        return Point.of(finalX, finalY);
    }

}
