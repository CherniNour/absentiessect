package com.example.absentiessect1.Models;
public class Reclamation {
    private String id;
    private String subject;
    private String message;
    private String enseignantId; // Ajout de l'ID de l'enseignant

    public Reclamation() {
        // Constructeur par défaut nécessaire pour Firebase
    }

    public Reclamation(String id, String subject, String message, String enseignantId) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.enseignantId = enseignantId;
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getEnseignantId() { return enseignantId; }
    public void setEnseignantId(String enseignantId) { this.enseignantId = enseignantId; }
}
