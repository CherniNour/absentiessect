package com.example.absentiessect1.Agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.absentiessect1.Admin.DashboardAdmin;
import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;


public class DashboardAgent extends AppCompatActivity {
    private static final String PREF_NAME = "UserPref";
    private TextView greetingText;
    private CardView ajouterAbsenceCardView;
    private CardView listeAbsencesCardView;
    private CardView gestionEmploiCardView;
    private  CardView logoutCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_agent);

        // Set up window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize TextView
        greetingText = findViewById(R.id.greeting_text);

        // Initialize CardViews
        ajouterAbsenceCardView = findViewById(R.id.card_ajouter_absence);
        listeAbsencesCardView = findViewById(R.id.card_liste_absences);
        logoutCard = findViewById(R.id.card_logout);

        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");
        String lastName = sharedPreferences.getString("lastName", "");
        String role = sharedPreferences.getString("role", "Unknown Role");

        // Set the greeting message
        String greetingMessage = "Bienvenue à AbsentiESSECT, " + name + " " + lastName + "\n" + role;
        greetingText.setText(greetingMessage);
        // Set OnClickListener for "Ajouter Absence"
        ajouterAbsenceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AjouterAbsence activity
                Intent intent = new Intent(DashboardAgent.this, AjouterAbsence.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for "Liste Absences"
        listeAbsencesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ListeAbsences activity
                Intent intent = new Intent(DashboardAgent.this, ListeAbsencesAgent.class);
                startActivity(intent);
            }
        });
        logoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAgent.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Finish the current activity
        });
        gestionEmploiCardView = findViewById(R.id.card_gestion_emploi);
        gestionEmploiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers l'activité GestionEmploi
                Intent intent = new Intent(DashboardAgent.this, GestionEmploi.class);
                startActivity(intent);
            }
        });

    }

}
