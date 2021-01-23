package pl.edu.pw.ee.aisd.pandemic.simulation;

import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.List;

public final class Ambulance {

    private double distanceTravelled;
    private Point currentPosition;
    private Patient currentPatient;

    private List<RoadNode> currentRoute;

    public double getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public void travel(final double distance) {
        this.distanceTravelled += distance;
    }

    public Point getCurrentPosition() {
        return this.currentPosition;
    }

    public void move(final Point point) {
        this.currentPosition = point;
    }

    public Patient getCurrentPatient() {
        return this.currentPatient;
    }

    public void loadNewPatient(final Patient patient) {
        this.currentPatient = patient;
        this.distanceTravelled = 0.0D;
    }

    public void empty() {
        this.currentPatient = null;
    }

    public void setNewRoute(final List<RoadNode> route) {
        this.currentRoute = route;
    }

    public RoadNode getLastStop() {
        return this.currentRoute.get(0);
    }

    public RoadNode getNextStop() {
        return this.currentRoute.get(1);
    }

    public void removeLastStop() {
        this.currentRoute.remove(0);
    }

    public int getRouteLength() {
        return this.currentRoute.size();
    }

}
