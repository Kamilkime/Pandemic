package pl.edu.pw.ee.aisd.pandemic.road.node;

import pl.edu.pw.ee.aisd.pandemic.patient.Patient;

public final class PickupPoint extends RoadNode {

    public PickupPoint(final Patient patient) {
        super(patient.getX(), patient.getY());
    }

}
