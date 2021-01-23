package pl.edu.pw.ee.aisd.pandemic.road.node;

import pl.edu.pw.ee.aisd.pandemic.point.Point;

import java.util.HashMap;
import java.util.Map;

public abstract class RoadNode extends Point {

    private final Map<RoadNode, Double> connections = new HashMap<>();

    protected RoadNode(final double x, final double y) {
        super(x, y);
    }

    public Map<RoadNode, Double> getConnections() {
        return this.connections;
    }

    public void addConnection(final RoadNode roadNode, final double distance) {
        this.connections.put(roadNode, distance);
    }

    public double getDistance(final RoadNode roadNode) {
        return this.connections.getOrDefault(roadNode, -1.0D);
    }

}
