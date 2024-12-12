package com.example.absentiessect1.Models;
public class Reclamation {
    private String id;
    private String subject;
    private String message;
    private String etat;

    private String enseignantId;

    public Reclamation() {
        // Constructeur par défaut nécessaire pour Firebase
    }

    public Reclamation(String id, String subject, String message, String enseignantId, String etat) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.enseignantId = enseignantId;
        this.etat = etat;
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public String getEtat() { return etat; }

    public void setMessage(String message) { this.message = message; }
    public void setEtat(String etat) { this.etat = etat; }


    public String getEnseignantId() { return enseignantId; }
    public void setEnseignantId(String enseignantId) { this.enseignantId = enseignantId; }
}