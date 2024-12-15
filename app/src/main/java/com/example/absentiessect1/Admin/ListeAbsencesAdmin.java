package com.example.absentiessect1.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ListeAbsencesAdmin extends AppCompatActivity {

    private LinearLayout absenceLayout; // Layout pour afficher dynamiquement les cartes
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_absences_admin);

        // Initialisation
        absenceLayout = findViewById(R.id.absence_layout);
        db = FirebaseFirestore.getInstance();

        // Charger les données des absences
        loadAllAbsences();
    }

    private void loadAllAbsences() {
        db.collection("Absences") // Collection Firestore
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    absenceLayout.removeAllViews(); // Nettoyer le layout avant d'ajouter les nouvelles données

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Récupérer les données d'une absence
                        String enseignant = document.getString("Enseignant");
                        String classe = document.getString("Classe");
                        String date = document.getString("Date");
                        String heure = document.getString("Heure");
                        String salle = document.getString("Salle");

                        // Ajouter la carte pour cette absence
                        addAbsenceCard(enseignant, classe, date, heure, salle);
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    private void addAbsenceCard(String enseignant, String classe, String date, String heure, String salle) {
        // Créer dynamiquement un CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(8, 8, 8, 8);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setUseCompatPadding(true);

        // Conteneur interne
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);

        // Texte pour afficher les informations
        TextView enseignantText = new TextView(this);
        enseignantText.setText("Enseignant: " + enseignant);
        enseignantText.setTextSize(18);
        enseignantText.setPadding(0, 0, 0, 8);

        TextView classeText = new TextView(this);
        classeText.setText("Classe: " + classe);
        classeText.setTextSize(16);
        classeText.setPadding(0, 0, 0, 8);

        TextView detailsText = new TextView(this);
        detailsText.setText("Date: " + date + "\nHeure: " + heure + "\nSalle: " + salle);
        detailsText.setTextSize(14);
        detailsText.setVisibility(View.GONE); // Masqué par défaut

        // Événement pour afficher/masquer les détails
        cardView.setOnClickListener(v -> {
            if (detailsText.getVisibility() == View.GONE) {
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        });

        // Ajouter les textes au conteneur
        cardContent.addView(enseignantText);
        cardContent.addView(classeText);
        cardContent.addView(detailsText);

        // Ajouter le conteneur au CardView
        cardView.addView(cardContent);

        // Ajouter le CardView au layout principal
        absenceLayout.addView(cardView);
    }
}