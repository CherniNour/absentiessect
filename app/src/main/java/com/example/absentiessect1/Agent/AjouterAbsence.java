package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AjouterAbsence extends AppCompatActivity {

    private EditText dateInput, timeInput, salleInput, classeInput, agentIdInput, enseignantInput;
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

        // Get references to the input fields
        dateInput = findViewById(R.id.date_input);
        timeInput = findViewById(R.id.time_input);
        salleInput = findViewById(R.id.salle_input);
        classeInput = findViewById(R.id.classe_input);
        agentIdInput = findViewById(R.id.agent_id_input);
        enseignantInput = findViewById(R.id.enseignant_input);
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

        // Set the click listener for the "Add Absence" button
        addAbsenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();
                String salle = salleInput.getText().toString();
                String classe = classeInput.getText().toString();
                String agentId = agentIdInput.getText().toString();
                String enseignant = enseignantInput.getText().toString(); // Get the enseignant input

                // Check if all fields are filled out
                if (date.isEmpty() || time.isEmpty() || salle.isEmpty() || classe.isEmpty() || agentId.isEmpty() || enseignant.isEmpty()) {
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
                absence.put("Enseignant", enseignant);  // Add the enseignant data

                // Save the absence to Firebase Firestore
                db.collection("Absences")
                        .add(absence)
                        .addOnSuccessListener(documentReference -> {
                            // Show success message
                            Toast.makeText(AjouterAbsence.this, "Absence ajoutée avec succès!", Toast.LENGTH_SHORT).show();
                            // Optionally, clear the input fields after submission
                            dateInput.setText("");
                            timeInput.setText("");
                            salleInput.setText("");
                            classeInput.setText("");
                            agentIdInput.setText("");
                            enseignantInput.setText("");  // Clear enseignant field
                        })
                        .addOnFailureListener(e -> {
                            // Show error message
                            Toast.makeText(AjouterAbsence.this, "Erreur lors de l'ajout de l'absence.", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}

