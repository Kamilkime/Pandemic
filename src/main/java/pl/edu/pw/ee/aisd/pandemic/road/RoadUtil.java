package pl.edu.pw.ee.aisd.pandemic.road;

import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.road.node.Intersection;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;
import pl.edu.pw.ee.aisd.pandemic.util.VectorUtil;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public final class RoadUtil {

    public static void checkIntersections(final Road newRoad, final Set<Road> roads) {
        final Iterator<Road> roadIterator = roads.iterator();
        while (roadIterator.hasNext()) {
            final Road road = roadIterator.next();
            if (road.equals(newRoad)) {
                continue;
            }

            if (newRoad.checkRelation(road) == RoadRelation.COMMON_POINT) {
                continue;
            }

            final Optional<Point> intersectionOptional = VectorUtil.intersectVectors(newRoad.getStart(), newRoad.getFinish(),
                    road.getStart(), road.getFinish());

            if (!intersectionOptional.isPresent()) {
                continue;
            }

            roadIterator.remove();

            final Intersection intersection = new Intersection(intersectionOptional.get());
            final RoadNode thisStart = newRoad.getStart();
            final RoadNode thisFinish = newRoad.getFinish();
            final RoadNode thatStart = road.getStart();
            final RoadNode thatFinish = road.getFinish();

            final double oldLengthThis = thisStart.distance(thisFinish);
            final double oldLengthThat = thatStart.distance(thatFinish);

            final Road road1 = new Road(intersection, thisStart, thisStart.distance(intersection) / oldLengthThis * newRoad.getDistance());
            final Road road2 = new Road(intersection, thatStart, thatStart.distance(intersection) / oldLengthThat * road.getDistance());
            final Road road3 = new Road(intersection, thisFinish, thisFinish.distance(intersection) / oldLengthThis * newRoad.getDistance());
            final Road road4 = new Road(intersection, thatFinish, thatFinish.distance(intersection) / oldLengthThat * road.getDistance());

            roads.add(road2);
            roads.add(road4);

            RoadUtil.checkIntersections(road1, roads);
            RoadUtil.checkIntersections(road3, roads);

            return;
        }

        roads.add(newRoad);
    }

    private RoadUtil() {}

}
