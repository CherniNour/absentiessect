package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AjouterAbsence extends AppCompatActivity {

    private EditText dateInput, timeInput, classeInput, agentIdInput;
    private Spinner salleSpinner; // Remplace EditText par Spinner
    private AutoCompleteTextView enseignantAutoComplete;
    private Button addAbsenceButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_absence);

        // Initialize Firebase Firestore and Authentication
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get references to the input fields and AutoCompleteTextView
        dateInput = findViewById(R.id.date_input);
        timeInput = findViewById(R.id.time_input);
        salleSpinner = findViewById(R.id.salle_spinner);  // Spinner for salle
        classeInput = findViewById(R.id.classe_input);
        agentIdInput = findViewById(R.id.agent_id_input);
        enseignantAutoComplete = findViewById(R.id.enseignant_autocomplete);
        addAbsenceButton = findViewById(R.id.btn_add_absence);

        // Get the current user (agent)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String agentId = currentUser.getUid();  // Get the unique ID of the current user
            agentIdInput.setText(agentId);          // Set it to the EditText
            agentIdInput.setEnabled(false);         // Disable the EditText so it can't be modified
        } else {
            Toast.makeText(this, "Utilisateur non connecté.", Toast.LENGTH_SHORT).show();
        }

        // Fetch teachers (enseignants) from Firestore and populate the AutoCompleteTextView
        fetchEnseignants();

        // Load salles from salles.txt into the Spinner
        loadSalles();

        // Set the click listener for the "Add Absence" button
        addAbsenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();
                String salle = salleSpinner.getSelectedItem().toString(); // Get the selected salle from Spinner
                String classe = classeInput.getText().toString();
                String agentId = agentIdInput.getText().toString();
                String enseignant = enseignantAutoComplete.getText().toString(); // Get the selected teacher from the AutoCompleteTextView

                // Check if all fields are filled out
                if (date.isEmpty() || time.isEmpty() || salle.isEmpty() || classe.isEmpty() || enseignant.isEmpty()) {
                    Toast.makeText(AjouterAbsence.this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Map to hold the absence data
                Map<String, Object> absence = new HashMap<>();
                absence.put("Date", date);
                absence.put("Heure", time);
                absence.put("Salle", salle);
                absence.put("Classe", classe);
                absence.put("IDagent", agentId);
                absence.put("Enseignant", enseignant);

                // Save the absence to Firebase Firestore
                db.collection("Absences")
                        .add(absence)
                        .addOnSuccessListener(documentReference -> {
                            // Show success message
                            Toast.makeText(AjouterAbsence.this, "Absence ajoutée avec succès!", Toast.LENGTH_SHORT).show();
                            // Optionally, clear the input fields after submission
                            dateInput.setText("");
                            timeInput.setText("");
                            classeInput.setText("");
                            enseignantAutoComplete.setText(""); // Clear the AutoCompleteTextView
                            salleSpinner.setSelection(0); // Reset spinner selection
                        })
                        .addOnFailureListener(e -> {
                            // Show error message
                            Toast.makeText(AjouterAbsence.this, "Erreur lors de l'ajout de l'absence.", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void loadSalles() {
        // Read salles from salles.txt in raw folder
        ArrayList<String> sallesList = new ArrayList<>();
        try {
            // Use InputStreamReader to read from raw folder
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.salles));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                sallesList.add(line.trim()); // Trim any spaces/newlines
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de lecture du fichier salles.txt", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the adapter for the salle Spinner
        ArrayAdapter<String> salleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sallesList);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salleSpinner.setAdapter(salleAdapter);
    }


    private void fetchEnseignants() {
        // Fetch users with the role "Enseignant" from Firestore
        db.collection("users")
                .whereEqualTo("role", "Enseignant")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> enseignantsList = new ArrayList<>();
                    for (var document : queryDocumentSnapshots) {
                        String lastName = document.getString("lastName");
                        String name = document.getString("name");
                        enseignantsList.add(name + " " + lastName);
                    }

                    ArrayAdapter<String> enseignantsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, enseignantsList);
                    enseignantAutoComplete.setAdapter(enseignantsAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AjouterAbsence.this, "Erreur de récupération des enseignants.", Toast.LENGTH_SHORT).show();
                });
    }
}
