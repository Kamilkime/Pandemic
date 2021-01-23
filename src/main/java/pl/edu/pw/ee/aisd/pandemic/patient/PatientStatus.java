package pl.edu.pw.ee.aisd.pandemic.patient;

import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;

import java.util.function.Function;

public enum PatientStatus {

    WAITING(hospital -> "OCZEKUJE NA TRANSPORT"),
    FINDING_HOSPITAL(hospital -> "OCZEKUJE NA WYBÓR SZPITALA"),
    IN_TRANSIT(hospital -> "W TRANSPORCIE DO SZPITALA #" + hospital.getID()),
    IN_HOSPITAL(hospital -> "W SZPITALU #" + hospital.getID()),
    IN_QUEUE(hospital -> "W KOLEJCE SZPITALA #" + hospital.getID()),
    OUTSIDE_BORDERS(hospital -> "POZA GRANICAMI OBSZARU");

    private final Function<Hospital, String> statusStringFunction;

    PatientStatus(final Function<Hospital, String> statusStringFunction) {
        this.statusStringFunction = statusStringFunction;
    }

    public String getStatusString(final Hospital hospital) {
        return this.statusStringFunction.apply(hospital);
    }

}
