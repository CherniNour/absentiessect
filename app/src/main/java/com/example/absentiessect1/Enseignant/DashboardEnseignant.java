package com.example.absentiessect1.Enseignant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

// Import the CardView class
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.Admin.DashboardAdmin;
import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;

public class DashboardEnseignant extends AppCompatActivity {
    private static final String PREF_NAME = "UserPref";
    private TextView greetingText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_enseignant);
        // Initialize TextView
        greetingText = findViewById(R.id.greeting_text);
        // Get references to the CardViews
        CardView cardAbsences = findViewById(R.id.card_absences);
        CardView cardNotifications = findViewById(R.id.card_notifications);
        CardView cardAddReclamation = findViewById(R.id.card_add_notification);
        CardView cardLogout = findViewById(R.id.card_logout);
        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");
        String lastName = sharedPreferences.getString("lastName", "");
        String role = sharedPreferences.getString("role", "Unknown Role");

        // Set the greeting message
        String greetingMessage = "Bienvenue à AbsentiESSECT, " + name + " " + lastName + "\n" + role;
        greetingText.setText(greetingMessage);
        // Set onClickListeners for each CardView
        cardAbsences.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, ListeAbsencesEnseignant.class);
            startActivity(intent);
        });

        cardNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, NotificationsActivity.class);
            startActivity(intent);
        });

        cardAddReclamation.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, ReclamationEnseignant.class);
            startActivity(intent);
        });
        cardLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Finish the current activity
        });

    }
}
