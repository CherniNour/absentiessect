package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GestionEmploi extends AppCompatActivity {

    private Button btnImporterExcel, btnImporterPdf, btnViewEmploi;
    private Spinner spinnerSalle;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_emploi);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        btnImporterExcel = findViewById(R.id.btn_importer_excel);
        btnImporterPdf = findViewById(R.id.btn_importer_pdf);
        btnViewEmploi = findViewById(R.id.btn_view_emploi);
        spinnerSalle = findViewById(R.id.spinner_salle);

        // Load salles from Firestore
        chargerSallesDepuisFirestore();

        // Set click listeners
        btnImporterExcel.setOnClickListener(v -> ouvrirGestionImportation("Excel"));
        btnImporterPdf.setOnClickListener(v -> ouvrirGestionImportation("PDF"));
        btnViewEmploi.setOnClickListener(v -> {
            String selectedSalle = (String) spinnerSalle.getSelectedItem();
            if (selectedSalle != null) {
                afficherEmploiDuTemps(selectedSalle);
            } else {
                Toast.makeText(this, "Aucune salle sélectionnée", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ouvrirGestionImportation(String type) {
        Intent intent = new Intent(GestionEmploi.this, ImporterFichier.class);
        intent.putExtra("TYPE_FICHIER", type);
        startActivity(intent);
    }

    private void afficherEmploiDuTemps(String salle) {
        Intent intent = new Intent(GestionEmploi.this, afficheremploiactivity.class);
        intent.putExtra("SALLE", salle);
        startActivity(intent);
    }

    private void chargerSallesDepuisFirestore() {
        firestore.collection("emploi")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> sallesList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String salle = document.getString("salle");
                            if (salle != null && !sallesList.contains(salle)) {
                                sallesList.add(salle);
                            }
                        }
                        populateSpinner(sallesList);
                    } else {
                        Toast.makeText(this, "Erreur lors du chargement des salles", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateSpinner(List<String> sallesList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sallesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalle.setAdapter(adapter);
    }
}
