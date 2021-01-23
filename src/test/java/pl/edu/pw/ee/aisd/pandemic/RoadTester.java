package pl.edu.pw.ee.aisd.pandemic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.road.Road;
import pl.edu.pw.ee.aisd.pandemic.road.RoadUtil;
import pl.edu.pw.ee.aisd.pandemic.road.node.Intersection;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.HashSet;
import java.util.Set;

public class RoadTester {

    @Test
    public void testRoadPartitioning() {
        final RoadNode a = new Intersection(Point.of(2, 4));
        final RoadNode b = new Intersection(Point.of(3, 6));
        final RoadNode c = new Intersection(Point.of(3, 1));
        final RoadNode d = new Intersection(Point.of(7, 4));

        final Road road1 = new Road(a, d, 500);
        final Road road2 = new Road(b, c, 500);

        final Set<Road> roads = new HashSet<>();
        roads.add(road1);

        RoadUtil.checkIntersections(road2, roads);
        Assertions.assertEquals(4, roads.size());

        for (final Road road : roads) {
            if (road.getDistance() == 100.0D) {
                Assertions.assertEquals(road.getFinish(), a);
            } else if (road.getDistance() == 200.0D) {
                Assertions.assertEquals(road.getFinish(), b);
            } else if (road.getDistance() == 300.0D) {
                Assertions.assertEquals(road.getFinish(), c);
            } else if (road.getDistance() == 400.0D) {
                Assertions.assertEquals(road.getFinish(), d);
            } else {
                Assertions.fail();
            }
        }
    }

}
