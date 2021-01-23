package pl.edu.pw.ee.aisd.pandemic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd.pandemic.hospital.HospitalConnection;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.road.node.Intersection;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;
import pl.edu.pw.ee.aisd.pandemic.util.RouteUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RouteTester {

    @Test
    public void testRoute() {
        final Intersection a = new Intersection(Point.of(-2, -2));
        final Intersection b = new Intersection(Point.of(2, 2));
        final Intersection c = new Intersection(Point.of(2, -2));
        final Intersection d = new Intersection(Point.of(-2, 2));
        final Intersection e = new Intersection(Point.of(8, 5));
        final Intersection f = new Intersection(Point.of(6, 1));
        final Intersection g = new Intersection(Point.of(4, 4));

        a.addConnection(c, 100);
        c.addConnection(a, 100);

        c.addConnection(d, 100);
        d.addConnection(c, 100);

        b.addConnection(d, 100);
        d.addConnection(b, 100);

        b.addConnection(g, 100);
        g.addConnection(b, 100);

        e.addConnection(g, 100);
        g.addConnection(e, 100);

        b.addConnection(f, 100);
        f.addConnection(b, 100);

        c.addConnection(f, 300);
        f.addConnection(c, 300);

        e.addConnection(f, 300);
        f.addConnection(e, 300);

        a.addConnection(d, 250);
        d.addConnection(a, 250);

        final List<RoadNode> route = RouteUtil.findRoute(a, e, new HashSet<>(Arrays.asList(a, b, c, d, e, f, g)));
        final HospitalConnection connection = new HospitalConnection(route);

        Assertions.assertIterableEquals(Arrays.asList(a, c, d, b, g, e), route);
        Assertions.assertEquals(500.0D, connection.getTotalDistance());
    }

}
