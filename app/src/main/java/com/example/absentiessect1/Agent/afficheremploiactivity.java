package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class afficheremploiactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficheremploiactivity);

        // Retrieve salle from intent
        String salle = getIntent().getStringExtra("SALLE");

        if (salle != null) {
            // Fetch and display emploi from Firestore for the selected salle
            fetchEmploiForSalle(salle);
        } else {
            Toast.makeText(this, "Aucune salle sélectionnée.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetch emploi du temps for the selected salle from Firestore.
     */
    private void fetchEmploiForSalle(String salle) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Query Firestore for the data related to the salle
        firestore.collection("emploi")
                .whereEqualTo("salle", salle)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(afficheremploiactivity.this, "Aucun emploi du temps trouvé pour cette salle.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the base64 string of the file (or any other data from Firestore)
                            String emploiBase64 = documentSnapshot.getString("fileBase64");
                            if (emploiBase64 != null) {
                                byte[] decodedBytes = Base64.decode(emploiBase64, Base64.DEFAULT);
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

                                // Log the content or perform any other operation
                                logExcelContent(inputStream);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(afficheremploiactivity.this, "Erreur lors de la récupération de l'emploi", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Method to read the Excel file and log its content for debugging.
     */
    private void logExcelContent(ByteArrayInputStream inputStream) {
        try {
            // Read the Excel file with Apache POI
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // Read the first sheet

            // Iterate over the rows and cells, logging their content
            for (Row row : sheet) {
                StringBuilder rowContent = new StringBuilder();
                for (Cell cell : row) {
                    rowContent.append(cell.toString()).append(" | ");
                }
                Log.d("afficheremploiactivity", rowContent.toString());
            }

            workbook.close();
        } catch (IOException e) {
            Log.e("afficheremploiactivity", "Error reading Excel file", e);
            Toast.makeText(this, "Erreur lors de la lecture du fichier Excel.", Toast.LENGTH_SHORT).show();
        }
    }
}
