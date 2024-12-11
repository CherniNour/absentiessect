package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

        // Charger les salles depuis le fichier `salles.txt`
        List<String> sallesList = chargerSallesDepuisFichier();
        if (sallesList != null) {
            // Créer un ArrayAdapter pour le Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, sallesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Définir l'adaptateur sur le Spinner
            spinnerSalle.setAdapter(adapter);

            // Limiter le nombre de lignes visibles dans le dropdown (4)
            spinnerSalle.setDropDownVerticalOffset(4); // Limite à 4 éléments visibles dans le Spinner

        } else {
            Toast.makeText(this, "Erreur lors du chargement des salles", Toast.LENGTH_SHORT).show();
        }

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
        // Vous pouvez passer la salle à l'activité suivante pour afficher l'emploi du temps
        Intent intent = new Intent(GestionEmploi.this, afficheremploiactivity.class);
        intent.putExtra("SALLE", salle);
        startActivity(intent);
    }

    // Méthode pour charger les salles depuis le fichier `salles.txt` dans `raw`
    private List<String> chargerSallesDepuisFichier() {
        List<String> sallesList = new ArrayList<>();
        try {
            // Ouvrir le fichier salles.txt dans le dossier raw
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.salles));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                sallesList.add(line.trim()); // Ajouter la salle à la liste (trim pour enlever les espaces inutiles)
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'erreur de lecture
        }
        return sallesList; // Retourner la liste des salles lues
    }
}
