package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;

public class AbsenceDetailsActivity extends AppCompatActivity {

    private TextView textEnseignant, textClasse, textDate, textHeure, textSalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_details);

        // Initialize TextViews
        textEnseignant = findViewById(R.id.text_enseignant);
        textClasse = findViewById(R.id.text_classe);
        textDate = findViewById(R.id.text_date);
        textHeure = findViewById(R.id.text_heure);
        textSalle = findViewById(R.id.text_salle);

        // Retrieve the Absence object passed from the previous activity
        Absence absence = (Absence) getIntent().getSerializableExtra("absenceDetails");

        // Set the values to the TextViews
        if (absence != null) {
            textEnseignant.setText("Enseignant: " + absence.getEnseignantNom());
            textClasse.setText("Classe: " + absence.getClasse());
            textDate.setText("Date: " + absence.getDate());
            textHeure.setText("Heure: " + absence.getHeure());
            textSalle.setText("Salle: " + absence.getSalle());
        }
    }
}
