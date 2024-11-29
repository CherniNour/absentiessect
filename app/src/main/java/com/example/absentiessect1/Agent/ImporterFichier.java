package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImporterFichier extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TextView tvFichierChemin;
    private Uri fichierUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer_fichier);

        Button btnChoisirFichier = findViewById(R.id.btn_choisir_fichier);
        Button btnTraiterFichier = findViewById(R.id.btn_traiter_fichier);
        tvFichierChemin = findViewById(R.id.tv_fichier_chemin);

        // Bouton pour sélectionner un fichier
        btnChoisirFichier.setOnClickListener(v -> choisirFichier());

        // Bouton pour traiter le fichier
        btnTraiterFichier.setOnClickListener(v -> {
            if (fichierUri != null) {
                traiterFichier();
            } else {
                Toast.makeText(this, "Veuillez sélectionner un fichier d'abord", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour ouvrir le sélecteur de fichiers
    private void choisirFichier() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Peut être affiné à "application/pdf" ou "application/vnd.ms-excel"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fichierUri = data.getData();
            if (fichierUri != null) {
                String fichierNom = obtenirNomFichier(fichierUri);
                tvFichierChemin.setText(fichierNom);
            }
        }
    }

    // Méthode pour obtenir le nom du fichier
    private String obtenirNomFichier(Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "Erreur : URI non valide", Toast.LENGTH_SHORT).show();
            return null;
        }

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) { // Vérifier si l'index est valide
                        return cursor.getString(nameIndex);
                    } else {
                        Toast.makeText(this, "Impossible de récupérer le nom du fichier", Toast.LENGTH_SHORT).show();
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return "Nom de fichier inconnu";
    }

    // Méthode pour traiter le fichier
    private void traiterFichier() {
        try (InputStream inputStream = getContentResolver().openInputStream(fichierUri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
            Log.d("ImporterFichier", "Contenu du fichier : \n" + contenu);

            // Ici, vous pouvez ajouter des traitements pour Excel ou PDF
            Toast.makeText(this, "Fichier traité avec succès", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("ImporterFichier", "Erreur lors du traitement du fichier", e);
            Toast.makeText(this, "Erreur lors du traitement du fichier", Toast.LENGTH_SHORT).show();
        }
    }
}
