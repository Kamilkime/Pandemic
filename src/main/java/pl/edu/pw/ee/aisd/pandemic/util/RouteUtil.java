package pl.edu.pw.ee.aisd.pandemic.util;

import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.*;

public final class RouteUtil {

    // https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
    public static List<RoadNode> findRoute(final RoadNode start, final RoadNode finish, final Set<RoadNode> nodes) {
        final Set<RoadNode> unvisitedNodes = new HashSet<>(nodes);
        final Map<RoadNode, Double> distance = new HashMap<>();
        final Map<RoadNode, RoadNode> previous = new HashMap<>();

        distance.put(start, 0.0D);

        while (!unvisitedNodes.isEmpty()) {
            RoadNode nextNode = null;

            for (final RoadNode node : unvisitedNodes) {
                final Double nodeDistance = distance.get(node);
                if (nodeDistance == null) {
                    continue;
                }

                if (nextNode == null || nodeDistance < distance.get(nextNode)) {
                    nextNode = node;
                }
            }

            if (nextNode == null) {
                break;
            }

            unvisitedNodes.remove(nextNode);

            for (final Map.Entry<RoadNode, Double> connection : nextNode.getConnections().entrySet()) {
                if (!unvisitedNodes.contains(connection.getKey())) {
                    continue;
                }

                final double newDistance = distance.get(nextNode) + connection.getValue();
                final Double neighbourDistance = distance.get(connection.getKey());

                if (neighbourDistance == null || newDistance < neighbourDistance) {
                    distance.put(connection.getKey(), newDistance);
                    previous.put(connection.getKey(), nextNode);
                }
            }
        }

        final List<RoadNode> route = new ArrayList<>();
        RoadNode prev = finish;

        if (previous.get(prev) != null || prev.equals(start)) {
            while (prev != null) {
                route.add(prev);
                prev = previous.get(prev);
            }
        }

        Collections.reverse(route);
        return route;
    }

    private RouteUtil() {}

}
