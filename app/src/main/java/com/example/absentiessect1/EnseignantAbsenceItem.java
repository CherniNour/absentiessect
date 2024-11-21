package com.example.absentiessect1;

public class EnseignantAbsenceItem {
    private String enseignantNom;
    private int absenceCount;

    public EnseignantAbsenceItem(String enseignantNom, int absenceCount) {
        this.enseignantNom = enseignantNom;
        this.absenceCount = absenceCount;
    }

    public String getEnseignantNom() {
        return enseignantNom;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }
}
