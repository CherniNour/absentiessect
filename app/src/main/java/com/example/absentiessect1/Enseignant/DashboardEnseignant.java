package com.example.absentiessect1.Enseignant;

import android.content.Intent;
import android.os.Bundle;

// Import the CardView class
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

public class DashboardEnseignant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_enseignant);

        // Get references to the CardViews
        CardView cardAbsences = findViewById(R.id.card_absences);
        CardView cardNotifications = findViewById(R.id.card_notifications);
        CardView cardAddReclamation = findViewById(R.id.card_add_notification);
        CardView cardLogout = findViewById(R.id.card_logout);

        // Set onClickListeners for each CardView
        cardAbsences.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, ListeAbsencesEnseignant.class);
            startActivity(intent);
        });

        /*cardNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, NotificationsEnseignant.class);
            startActivity(intent);
        });

        cardAddReclamation.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, ReclamationEnseignant.class);
            startActivity(intent);
        });*/

       /* cardLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEnseignant.this, LogoutActivity.class);
            startActivity(intent);
        });*/
    }
}
