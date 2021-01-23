package pl.edu.pw.ee.aisd.pandemic.data.file;

import pl.edu.pw.ee.aisd.pandemic.data.DataSection;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class PatientDataLoader {

    private final File inFile;
    private final Map<DataSection, List<String>> dataLines = new EnumMap<>(DataSection.class);

    public PatientDataLoader(final File inFile) {
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
                    new ErrorPopup("DUPLIKAT SEKCJI W PLIKU PACJENTÓW", currentSection.getSectionName()).show();
                    return false;
                }

                if (currentSection != DataSection.NONE) {
                    this.dataLines.put(currentSection, new ArrayList<>());
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

    public List<Patient> loadPatients() {
        final List<String> lines = this.dataLines.get(DataSection.PATIENTS);
        final List<Patient> patients = new ArrayList<>();

        if (lines == null || lines.isEmpty()) {
            new ErrorPopup("BRAKUJĄCA SEKCJA W PLIKU PACJENTÓW", DataSection.PATIENTS.getSectionName()).show();
            return patients;
        }

        for (final String line : lines) {
            try {
                patients.add(Patient.fromString(line));
            } catch (final IllegalArgumentException exception) {
                return new ArrayList<>();
            }
        }

        return patients;
    }

}
