package pl.edu.pw.ee.aisd.pandemic.data;

public enum DataSection {

    HOSPITALS("# Szpitale", "Szpitale"),
    MONUMENTS("# Obiekty", "Obiekty"),
    ROADS("# Drogi", "Drogi"),
    PATIENTS("# Pacjenci", "Pacjenci"),
    NONE("", "");

    private final String sectionHeader;
    private final String sectionName;

    DataSection(final String sectionHeader, final String sectionName) {
        this.sectionHeader = sectionHeader;
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public static DataSection getSection(final String sectionHeader) {
        if (sectionHeader.startsWith(HOSPITALS.sectionHeader)) {
            return HOSPITALS;
        }

        if (sectionHeader.startsWith(MONUMENTS.sectionHeader)) {
            return MONUMENTS;
        }

        if (sectionHeader.startsWith(ROADS.sectionHeader)) {
            return ROADS;
        }

        if (sectionHeader.startsWith(PATIENTS.sectionHeader)) {
            return PATIENTS;
        }

        return NONE;
    }

}
