package com.example.absentiessect1.Models;

public class Notification {
    private String message;
    private String date;
    private boolean isRead;  // Ajout du statut (lue/non lue)

    public Notification(String message, boolean isRead) {
        this.message = message;
        this.date = date;
        this.isRead = isRead;
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
