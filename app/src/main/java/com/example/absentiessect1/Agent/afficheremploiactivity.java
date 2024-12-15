package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class afficheremploiactivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private TableLayout tableLayout; // Reference to TableLayout from XML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficheremploiactivity);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Find TableLayout from XML
        tableLayout = findViewById(R.id.tableLayout);

        // Get selected salle from intent
        String selectedSalle = getIntent().getStringExtra("SALLE");
        if (selectedSalle != null) {
            afficherEmploiDepuisFirestore(selectedSalle);
        } else {
            Toast.makeText(this, "Aucune salle sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }

    private void afficherEmploiDepuisFirestore(String salle) {
        firestore.collection("emploi")
                .whereEqualTo("salle", salle)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String base64File = document.getString("fileBase64");
                                if (base64File != null) {
                                    displayExcelContent(base64File);
                                    break; // Display the first matching document
                                }
                            }
                        } else {
                            Toast.makeText(this, "Aucun emploi du temps trouvé pour cette salle", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Erreur lors du chargement de l'emploi du temps", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayExcelContent(String base64File) {
        try {
            // Decode Base64 file
            byte[] fileBytes = Base64.decode(base64File, Base64.DEFAULT);

            // Save the decoded bytes to an Excel file
            File excelFile = saveExcelToStorage(fileBytes);

            // Display content from Excel file
            if (excelFile != null) {
                readExcelContent(excelFile);
            } else {
                Toast.makeText(this, "Erreur lors de l'enregistrement du fichier Excel", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du décodage du fichier Excel", Toast.LENGTH_SHORT).show();
        }
    }

    private File saveExcelToStorage(byte[] fileBytes) {
        try {
            // Create a directory in external storage
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ExcelFiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create an Excel file in the directory
            File excelFile = new File(directory, "emploi_du_temps.xlsx");
            FileOutputStream fos = new FileOutputStream(excelFile);
            fos.write(fileBytes);
            fos.close();

            return excelFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void readExcelContent(File excelFile) {
        try {
            // Open the Excel file
            FileInputStream fis = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // Clear the previous content in the TableLayout
            tableLayout.removeAllViews();

            // Iterate through the rows to display timetable events
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // Create a new TableRow
                TableRow tableRow = new TableRow(this);

                // Iterate over the columns to populate the cells
                for (int colIndex = 0; colIndex < row.getPhysicalNumberOfCells(); colIndex++) {
                    String cellValue = row.getCell(colIndex) != null ? row.getCell(colIndex).toString() : "";

                    // Create a TextView for each cell
                    TextView cellTextView = new TextView(this);
                    cellTextView.setText(cellValue);
                    cellTextView.setGravity(android.view.Gravity.CENTER);
                    cellTextView.setPadding(8, 8, 8, 8);
                    cellTextView.setBackgroundColor(getResources().getColor(R.color.cellBackground)); // Customize as needed

                    // Add the TextView to the TableRow
                    tableRow.addView(cellTextView);
                }

                // Add the TableRow to the TableLayout
                tableLayout.addView(tableRow);
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la lecture du fichier Excel", Toast.LENGTH_SHORT).show();
        }
    }
}
