package pl.edu.pw.ee.aisd.pandemic.hospital;

import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.ArrayList;
import java.util.List;

public final class HospitalConnection {

    private final List<RoadNode> route;
    private final double totalDistance;

    public HospitalConnection(final List<RoadNode> route) {
        this.route = route;

        double distance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            distance += route.get(i).getDistance(route.get(i + 1));
        }

        this.totalDistance = distance;
    }

    public List<RoadNode> getRoute() {
        return new ArrayList<>(this.route);
    }

    public double getTotalDistance() {
        return this.totalDistance;
    }

}
