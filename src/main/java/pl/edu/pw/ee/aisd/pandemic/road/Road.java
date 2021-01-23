package pl.edu.pw.ee.aisd.pandemic.road;

import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Road {

    private final RoadNode start;
    private final RoadNode finish;

    private final double distance;
    private final Map<Road, RoadRelation> relations;

    public Road(final RoadNode start, final RoadNode finish, final double distance) {
        this.start = start;
        this.finish = finish;
        this.distance = distance;
        this.relations = new HashMap<>();
    }

    public RoadNode getStart() {
        return this.start;
    }

    public RoadNode getFinish() {
        return this.finish;
    }

    public double getDistance() {
        return this.distance;
    }

    public RoadRelation checkRelation(final Road road) {
        RoadRelation relation = this.relations.get(road);
        if (relation != null) {
            return relation;
        }

        final boolean commonNode1 = road.start.equals(this.start);
        final boolean commonNode2 = road.finish.equals(this.finish);
        final boolean commonNode3 = road.start.equals(this.finish);
        final boolean commonNode4 = road.finish.equals(this.start);

        if (commonNode1 && commonNode2 || commonNode3 && commonNode4) {
            relation = RoadRelation.COLLINEAR;
        } else if (commonNode1 || commonNode2 || commonNode3 || commonNode4) {
            relation = RoadRelation.COMMON_POINT;
        } else {
            relation = RoadRelation.NO_COMMON_POINT;
        }

        this.relations.put(road, relation);
        return relation;
    }

    public static Road fromString(final String line, final Map<Integer, Hospital> hospitals) throws IllegalArgumentException {
        String[] lineSplit = DataStorage.LINE_SPLIT_PATTERN.split(line);

        if (lineSplit.length == 4) {
            lineSplit = Arrays.copyOfRange(lineSplit, 1, 4);
        } else if (lineSplit.length != 3) {
            new ErrorPopup("ZŁA LICZBA PARAMETRÓW, WYMAGANE: 3", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final int startID;
        try {
            startID = Integer.parseInt(lineSplit[0]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("ID MUSI BYĆ LICZBĄ CAŁKOWITĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final Hospital startHospital;
        try {
            startHospital = hospitals.get(startID);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("BRAK SZPITALA O ID #" + startID, "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final int finishID;
        try {
            finishID = Integer.parseInt(lineSplit[1]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("ID MUSI BYĆ LICZBĄ CAŁKOWITĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final Hospital finishHospital;
        try {
            finishHospital = hospitals.get(finishID);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("BRAK SZPITALA O ID #" + finishID, "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final double distance;
        try {
            distance = Double.parseDouble(lineSplit[2]);

            if (distance <= 0.0D) {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException exception) {
            new ErrorPopup("DYSTANS MUSI BYĆ LICZBĄ RZECZYWISTĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        return new Road(startHospital, finishHospital, distance);
    }

}
