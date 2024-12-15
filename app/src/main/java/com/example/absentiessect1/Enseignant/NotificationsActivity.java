package com.example.absentiessect1.Enseignant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Notification;
import com.example.absentiessect1.NotificationAdapter;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // SharedPreferences to get user data
    private static final String PREF_NAME = "UserPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the current teacher's name and last name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String teacherName = sharedPreferences.getString("name", "");
        String teacherLastName = sharedPreferences.getString("lastName", "");

        // Fetch notifications for the logged-in teacher
        fetchNotifications(teacherName, teacherLastName);

        notificationAdapter = new NotificationAdapter(notificationList, new NotificationAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Notification notification) {
                // Handle notification click: mark as read
                updateNotificationStatus(notification);
            }
        });

        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(notificationAdapter);
    }

    private void fetchNotifications(String teacherName, String teacherLastName) {
        db.collection("Notification") // Assuming your notifications are in the "notifications" collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String message = document.getString("message");
                            boolean isRead = document.getBoolean("isRead") != null ? document.getBoolean("isRead") : false; // Handle null case

                            // Check if the notification message contains the teacher's name and last name
                            if (message != null && message.contains(teacherName) && message.contains(teacherLastName)) {
                                // Add the notification to the list if it matches
                                Notification notification = new Notification(message, isRead);
                                notificationList.add(notification);
                            }
                        }
                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NotificationsActivity.this, "Aucune notification trouvée.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de la récupération des notifications.", e);
                    Toast.makeText(NotificationsActivity.this, "Erreur lors de la récupération des notifications.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateNotificationStatus(Notification notification) {
        // Toggle read status and update Firestore
        notification.setRead(true); // Mark as read

        // Assuming you update the status directly in Firestore based on timestamp or message
        db.collection("Notification")
                .whereEqualTo("message", notification.getMessage())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().update("isRead", true)
                                    .addOnSuccessListener(aVoid -> {
                                        // Successfully marked as read, update UI
                                        notification.setRead(true);
                                        notificationAdapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Log.e("Firestore", "Failed to update status", e));
                        }
                    }
                });
    }
}
