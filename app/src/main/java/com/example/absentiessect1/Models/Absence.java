package com.example.absentiessect1.Models;

import java.io.Serializable;

public class Absence implements Serializable {
    private String Classe;
    private String Date;
    private String Heure;
    private String IDagent;
    private String Salle;
    private String Enseignant;


    // Default constructor required for Firestore's toObjects() method
    public Absence() {
    }

    // Getters and setters
    public String getClasse() {
        return Classe;
    }

    public void setClasse(String classe) {
        this.Classe = classe;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getHeure() {
        return Heure;
    }

    public void setHeure(String heure) {
        this.Heure = heure;
    }

    public String getIDagent() {
        return IDagent;
    }

    public void setIDagent(String IDagent) {
        this.IDagent = IDagent;
    }

    public String getSalle() {
        return Salle;
    }

    public void setSalle(String salle) {
        this.Salle = salle;
    }
    public String getEnseignantNom() {
        return Enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.Enseignant = enseignant;
    }

}

