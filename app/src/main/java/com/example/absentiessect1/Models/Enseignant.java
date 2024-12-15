package com.example.absentiessect1.Models;

import java.io.Serializable;

public class Enseignant implements Serializable {

    private String nom;
    private String details;
    private String email;  // Add the email field

    // Constructor required for Firestore deserialization
    public Enseignant() {
    }

    public Enseignant(String nom, String details, String email) {
        this.nom = nom;
        this.details = details;
        this.email = email;  // Initialize the email
    }

    // Getters and Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmail() {
        return email;  // Getter for email
    }

    public void setEmail(String email) {
        this.email = email;  // Setter for email
    }
}