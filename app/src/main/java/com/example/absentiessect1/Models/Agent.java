package com.example.absentiessect1.Models;

public class Agent {
    private String id, name, lastName, phone, email;
    private boolean isEditable = false;

    public Agent(String id, String name, String lastName, String phone, String email, boolean isEditable) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.isEditable = isEditable;
    }
    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}