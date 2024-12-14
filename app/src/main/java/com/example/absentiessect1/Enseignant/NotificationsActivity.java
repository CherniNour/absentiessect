package com.example.absentiessect1.Enseignant;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Notification;
import com.example.absentiessect1.NotificationAdapter;
import com.example.absentiessect1.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationList = new ArrayList<>();

        // Ajouter des données de test
        notificationList.add(new Notification("Absence d'un enseignant", "L'enseignant X est absent aujourd'hui.", "12/12/2024", false));
        notificationList.add(new Notification("Absence d'un enseignant", "L'enseignant Y est absent aujourd'hui.", "12/12/2024", true));

        notificationAdapter = new NotificationAdapter(notificationList);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(notificationAdapter);
    }
}
