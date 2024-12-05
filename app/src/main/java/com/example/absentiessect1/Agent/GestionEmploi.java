package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

public class GestionEmploi extends AppCompatActivity {

    private Button btnImporterExcel;
    private Button btnImporterPdf;
    private Button btnViewEmploi;
    private Spinner spinnerSalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_emploi);

        // Initialisation des vues
        btnImporterExcel = findViewById(R.id.btn_importer_excel);
        btnImporterPdf = findViewById(R.id.btn_importer_pdf);
        btnViewEmploi = findViewById(R.id.btn_view_emploi);
        spinnerSalle = findViewById(R.id.spinner_salle);

        // Initialisation des boutons
        btnImporterExcel.setOnClickListener(v -> ouvrirGestionImportation("Excel"));
        btnImporterPdf.setOnClickListener(v -> ouvrirGestionImportation("PDF"));

        // Spinner setup: You can populate this list with actual salle numbers from your database
        String[] salles = {"1", "Salle 102", "Salle 103"};  // Example data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, salles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalle.setAdapter(adapter);

        // Gestion du clic sur le bouton "Voir l'Emploi du Temps"
        btnViewEmploi.setOnClickListener(v -> {
            String selectedSalle = (String) spinnerSalle.getSelectedItem();
            if (selectedSalle != null) {
                afficherEmploiDuTemps(selectedSalle);
            }
        });
    }

    private void ouvrirGestionImportation(String type) {
        Intent intent = new Intent(GestionEmploi.this, ImporterFichier.class);
        intent.putExtra("TYPE_FICHIER", type);
        startActivity(intent);
    }

    private void afficherEmploiDuTemps(String salle) {
        // You can pass the salle to the next activity to retrieve and display the emploi du temps for that salle
        Intent intent = new Intent(GestionEmploi.this, afficheremploiactivity.class);
        intent.putExtra("SALLE", salle);
        startActivity(intent);
    }
}
