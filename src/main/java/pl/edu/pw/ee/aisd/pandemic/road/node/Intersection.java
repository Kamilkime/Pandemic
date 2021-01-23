package pl.edu.pw.ee.aisd.pandemic.road.node;

import pl.edu.pw.ee.aisd.pandemic.point.Point;

public final class Intersection extends RoadNode {

    public Intersection(final Point point) {
        super(point.getX(), point.getY());
    }

}
