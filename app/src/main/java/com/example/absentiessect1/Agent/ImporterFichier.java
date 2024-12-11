package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;

public class ImporterFichier extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TextView tvFichierChemin;
    private Uri fichierUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer_fichier);

        Button btnChoisirFichier = findViewById(R.id.btn_choisir_fichier);
        tvFichierChemin = findViewById(R.id.tv_fichier_chemin);

        // Bouton pour sélectionner un fichier
        btnChoisirFichier.setOnClickListener(v -> choisirFichier());
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
}
