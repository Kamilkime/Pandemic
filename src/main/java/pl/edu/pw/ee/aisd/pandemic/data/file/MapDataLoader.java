package pl.edu.pw.ee.aisd.pandemic.data.file;

import pl.edu.pw.ee.aisd.pandemic.data.DataSection;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.monument.Monument;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.road.Road;
import pl.edu.pw.ee.aisd.pandemic.road.RoadRelation;
import pl.edu.pw.ee.aisd.pandemic.road.RoadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class MapDataLoader {

    private final File inFile;
    private final Map<DataSection, Set<String>> dataLines = new EnumMap<>(DataSection.class);

    public MapDataLoader(final File inFile) {
        this.inFile = inFile;
    }

    public boolean loadLines() throws FileNotFoundException {
        final Scanner fileScanner = new Scanner(this.inFile);

        DataSection currentSection = DataSection.NONE;
        while (fileScanner.hasNextLine()) {
            final String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("#")) {
                currentSection = DataSection.getSection(line);

                if (this.dataLines.containsKey(currentSection)) {
                    new ErrorPopup("DUPLIKAT SEKCJI W PLIKU MAPY", currentSection.getSectionName()).show();
                    return false;
                }

                if (currentSection != DataSection.NONE) {
                    this.dataLines.put(currentSection, new HashSet<>());
                }

                continue;
            }

            if (currentSection == DataSection.NONE) {
                continue;
            }

            this.dataLines.get(currentSection).add(line);
        }

        fileScanner.close();
        return true;
    }

    public Map<Integer, Hospital> loadHospitals() {
        final Set<String> lines = this.dataLines.get(DataSection.HOSPITALS);
        final Map<Integer, Hospital> hospitals = new HashMap<>();

        if (lines == null || lines.isEmpty()) {
            new ErrorPopup("BRAKUJĄCA SEKCJA W PLIKU MAPY", DataSection.HOSPITALS.getSectionName()).show();
            return hospitals;
        }

        for (final String line : lines) {
            try {
                final Hospital hospital = Hospital.fromString(line);

                if (hospitals.containsKey(hospital.getID())) {
                    new ErrorPopup("DUPLIKAT ID SZPITALA", "LINIA: " + line).show();
                    throw new IllegalArgumentException();
                }

                hospitals.put(hospital.getID(), hospital);
            } catch (final IllegalArgumentException exception) {
                return new HashMap<>();
            }
        }

        return hospitals;
    }

    public Map<Integer, Monument> loadMonuments() {
        final Set<String> lines = this.dataLines.get(DataSection.MONUMENTS);
        final Map<Integer, Monument> monuments = new HashMap<>();

        if (lines == null || lines.isEmpty()) {
            new ErrorPopup("BRAKUJĄCA SEKCJA W PLIKU MAPY", DataSection.MONUMENTS.getSectionName()).show();
            return monuments;
        }

        for (final String line : lines) {
            try {
                final Monument monument = Monument.fromString(line);

                if (monuments.containsKey(monument.getID())) {
                    new ErrorPopup("DUPLIKAT ID OBIEKTU", "LINIA: " + line).show();
                    throw new IllegalArgumentException();
                }

                monuments.put(monument.getID(), monument);
            } catch (final IllegalArgumentException exception) {
                return new HashMap<>();
            }
        }

        return monuments;
    }

    public Set<Road> loadRoads(final Map<Integer, Hospital> hospitals) {
        final Set<String> lines = this.dataLines.get(DataSection.ROADS);

        final Set<Road> baseRoads = new HashSet<>();
        final Set<Road> roads = new HashSet<>();

        if (lines == null || lines.isEmpty()) {
            new ErrorPopup("BRAKUJĄCA SEKCJA W PLIKU MAPY", DataSection.ROADS.getSectionName()).show();
            return roads;
        }

        for (final String line : lines) {
            try {
                final Road newRoad = Road.fromString(line, hospitals);

                for (final Road road : baseRoads) {
                    if (newRoad.checkRelation(road) == RoadRelation.COLLINEAR) {
                        new ErrorPopup("DUPLIKAT DROGI", "LINIA: " + line).show();
                        throw new IllegalArgumentException();
                    }
                }

                baseRoads.add(newRoad);
                RoadUtil.checkIntersections(newRoad, roads);
            } catch (final IllegalArgumentException exception) {
                return new HashSet<>();
            }
        }

        return roads;
    }

}
