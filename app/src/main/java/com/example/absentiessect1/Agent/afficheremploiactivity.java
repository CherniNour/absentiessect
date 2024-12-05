package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class afficheremploiactivity extends AppCompatActivity {

    private TextView tvEmploi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficheremploiactivity);

        tvEmploi = findViewById(R.id.tv_emploi);

        // Retrieve salle from intent
        String salle = getIntent().getStringExtra("SALLE");

        if (salle != null) {
            // Fetch and display emploi from Firestore for the selected salle
            fetchEmploiForSalle(salle);
        } else {
            tvEmploi.setText("Aucune salle sélectionnée.");
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
                        tvEmploi.setText("Aucun emploi du temps trouvé pour cette salle.");
                    } else {
                        StringBuilder emploiDetails = new StringBuilder();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the base64 string of the file (or any other data from Firestore)
                            String emploiBase64 = documentSnapshot.getString("fileBase64");
                            if (emploiBase64 != null) {
                                byte[] decodedBytes = Base64.decode(emploiBase64, Base64.DEFAULT);
                                InputStream inputStream = new ByteArrayInputStream(decodedBytes);

                                // Read Excel content and set it to TextView
                               // String excelContent = readExcelFile(inputStream);
                               // emploiDetails.append(excelContent).append("\n");
                            }
                        }
                        tvEmploi.setText(emploiDetails.toString());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(afficheremploiactivity.this, "Erreur lors de la récupération de l'emploi", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Method to read the Excel file and convert it to string.
     */
   /* private String readExcelFile(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Read the Excel file with Apache POI
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(inputStream);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);  // Read the first sheet

            for (org.apache.poi.ss.usermodel.Row row : sheet) {
                for (org.apache.poi.ss.usermodel.Cell cell : row) {
                    // Handle different cell types (String, Numeric, etc.)
                    switch (cell.getCellType()) {
                        case STRING:
                            stringBuilder.append(cell.getStringCellValue()).append("\t");
                            break;
                        case NUMERIC:
                            stringBuilder.append(cell.getNumericCellValue()).append("\t");
                            break;
                        case BOOLEAN:
                            stringBuilder.append(cell.getBooleanCellValue()).append("\t");
                            break;
                        default:
                            stringBuilder.append("N/A").append("\t");
                            break;
                    }
                }
                stringBuilder.append("\n");
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier Excel.";
        }
        return stringBuilder.toString();
    }*/
}
