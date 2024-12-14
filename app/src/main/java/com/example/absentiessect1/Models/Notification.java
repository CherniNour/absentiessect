package com.example.absentiessect1.Models;

public class Notification {
    private String title;
    private String message;
    private String date;
    private boolean isRead;  // Ajout du statut (lue/non lue)

    public Notification(String title, String message, String date, boolean isRead) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
