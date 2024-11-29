package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

public class GestionEmploi extends AppCompatActivity {

    private Button btnImporterExcel;
    private Button btnImporterPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_emploi);

        // Initialisation des boutons
        btnImporterExcel = findViewById(R.id.btn_importer_excel);
        btnImporterPdf = findViewById(R.id.btn_importer_pdf);

        // Gestion du clic sur le bouton "Importer un fichier Excel"
        btnImporterExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter la logique pour importer un fichier Excel
                ouvrirGestionImportation("Excel");
            }
        });

        // Gestion du clic sur le bouton "Importer un fichier PDF"
        btnImporterPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter la logique pour importer un fichier PDF
                ouvrirGestionImportation("PDF");
            }
        });
    }

    /**
     * Méthode pour ouvrir une autre activité en fonction du type de fichier
     * @param type Type de fichier (Excel ou PDF)
     */
    private void ouvrirGestionImportation(String type) {
        Intent intent = new Intent(GestionEmploi.this, ImporterFichier.class);
        intent.putExtra("TYPE_FICHIER", type);
        startActivity(intent);
    }
}
