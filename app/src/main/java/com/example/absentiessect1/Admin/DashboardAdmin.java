package com.example.absentiessect1.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;

public class DashboardAdmin extends AppCompatActivity {
    private static final String PREF_NAME = "UserPref";
    private TextView greetingText;
    private CardView dashboardCard;
    private CardView listeAbsencesCard, listeReclamationsCard;
    private CardView notificationsCard, gestionUsersCard, logoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_admin);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize TextView
        greetingText = findViewById(R.id.greeting_text);

        // Initialize only the Cardviews
        gestionUsersCard = findViewById(R.id.users);
        listeAbsencesCard = findViewById(R.id.liste_abs);
        listeReclamationsCard = findViewById(R.id.reclamations);
        notificationsCard = findViewById(R.id.notifications);
        logoutCard = findViewById(R.id.card_logout);
        dashboardCard = findViewById(R.id.dashboard); // Make sure the ID matches with the XML
        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");
        String lastName = sharedPreferences.getString("lastName", "");
        String role = sharedPreferences.getString("role", "Unknown Role");

        // Set the greeting message
        String greetingMessage = "Bienvenue à AbsentiESSECT, " + name + " " + lastName + "\n" + role;
        greetingText.setText(greetingMessage);

        // Check if the dashboardCard is initialized and set the onClick listener
        if (dashboardCard != null) {
            dashboardCard.setOnClickListener(v -> navigateToActivity(Statistics.class)); // Replace with the desired activity
        } else {
            Log.e("DashboardAdmin", "dashboardCard is null");
        }
        // Set click listeners for each CardView
        listeAbsencesCard.setOnClickListener(v -> navigateToActivity(ListeAbsencesAdmin.class));
        listeReclamationsCard.setOnClickListener(v -> navigateToActivity(ListeReclamationsAdmin.class));
        notificationsCard.setOnClickListener(v -> navigateToActivity(NotificationsAdmin.class));
        gestionUsersCard.setOnClickListener(v -> navigateToActivity(AdminGestionUsers.class));
        logoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Finish the current activity
        });
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
}
