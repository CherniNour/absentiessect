package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ImporterFichier extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TextView tvFichierChemin;
    private EditText etSalle;
    private Uri fichierUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer_fichier);

        Button btnChoisirFichier = findViewById(R.id.btn_choisir_fichier);
        Button btnTraiterFichier = findViewById(R.id.btn_traiter_fichier);
        tvFichierChemin = findViewById(R.id.tv_fichier_chemin);
        etSalle = findViewById(R.id.et_salle); // New EditText for room information

        // Bouton pour sélectionner un fichier
        btnChoisirFichier.setOnClickListener(v -> choisirFichier());

        // Bouton pour traiter le fichier et ajouter les données à Firestore
        btnTraiterFichier.setOnClickListener(v -> {
            if (fichierUri != null) {
                String salle = etSalle.getText().toString().trim();
                if (!salle.isEmpty()) {
                    traiterEtAjouterFichier(salle);
                } else {
                    Toast.makeText(this, "Veuillez saisir une salle", Toast.LENGTH_SHORT).show();
                }
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

    // Méthode pour traiter le fichier et l'ajouter à Firestore
    private void traiterEtAjouterFichier(String salle) {
        try (InputStream inputStream = getContentResolver().openInputStream(fichierUri)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Lire le fichier et l'enregistrer dans un tableau d'octets
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convertir le tableau d'octets en Base64
            String base64String = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            // Ajouter les données à Firestore
            ajouterFichierFirestore(base64String, salle);

        } catch (Exception e) {
            Log.e("ImporterFichier", "Erreur lors du traitement du fichier", e);
            Toast.makeText(this, "Erreur lors du traitement du fichier", Toast.LENGTH_SHORT).show();
        }
    }

    private void ajouterFichierFirestore(String base64String, String salle) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("fileBase64", base64String);
        data.put("salle", salle);
        data.put("timestamp", System.currentTimeMillis());

        firestore.collection("emploi")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Fichier ajouté avec succès", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de l'ajout du fichier à Firestore", e);
                    Toast.makeText(this, "Erreur lors de l'ajout à Firestore", Toast.LENGTH_SHORT).show();
                });
    }
}