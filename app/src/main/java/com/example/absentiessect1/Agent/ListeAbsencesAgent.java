package com.example.absentiessect1.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Absence;
import com.example.absentiessect1.Enseignant.EnseignantAdapter;
import com.example.absentiessect1.EnseignantAbsenceItem;
import com.example.absentiessect1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeAbsencesAgent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private String currentAgentId;
    private List<EnseignantAbsenceItem> enseignantList = new ArrayList<>();
    private EnseignantAdapter enseignantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_absences_agent);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get the currently logged-in agent's ID
        currentAgentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.absences_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        enseignantAdapter = new EnseignantAdapter(enseignantList, this, new EnseignantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EnseignantAbsenceItem enseignantItem) {
                // Handle the click event to show detailed absences for the selected professor
                showAbsenceDetails(enseignantItem.getEnseignantNom(),new Absence());
            }
        });
        recyclerView.setAdapter(enseignantAdapter);

        // Load absences and calculate summaries
        loadAbsences();
    }

    private void loadAbsences() {
        db.collection("Absences")
                .whereEqualTo("IDagent", currentAgentId)  // Filter by the logged-in agent's ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Map to hold the count of absences per professor
                        Map<String, Integer> absenceCountByEnseignant = new HashMap<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the professor's name (Enseignant) and update their absence count
                            String enseignantNom = document.getString("Enseignant");
                            absenceCountByEnseignant.put(enseignantNom,
                                    absenceCountByEnseignant.getOrDefault(enseignantNom, 0) + 1);
                        }

                        // Prepare the list for the RecyclerView
                        enseignantList.clear();
                        for (Map.Entry<String, Integer> entry : absenceCountByEnseignant.entrySet()) {
                            enseignantList.add(new EnseignantAbsenceItem(entry.getKey(), entry.getValue()));
                        }

                        enseignantAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListeAbsencesAgent.this, "Aucune absence trouvée.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListeAbsencesAgent.this, "Erreur de chargement des absences.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showAbsenceDetails(String enseignantNom, Absence absence) {
        // Fetch more details about the selected absence from Firestore
        db.collection("Absences")
                .whereEqualTo("Enseignant", enseignantNom)  // Filter by the professor's name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Populate the Absence object with data from Firestore
                        Absence selectedAbsence = new Absence();
                        selectedAbsence.setEnseignant(document.getString("Enseignant"));
                        selectedAbsence.setClasse(document.getString("Classe"));
                        selectedAbsence.setDate(document.getString("Date"));
                        selectedAbsence.setHeure(document.getString("Heure"));
                        selectedAbsence.setSalle(document.getString("Salle"));

                        // Pass the Absence object to the next activity
                        Intent intent = new Intent(ListeAbsencesAgent.this, AbsenceDetailsActivity.class);
                        intent.putExtra("absenceDetails", selectedAbsence);  // Pass the Absence object
                        startActivity(intent);
                    } else {
                        Toast.makeText(ListeAbsencesAgent.this, "Aucune absence trouvée pour cet enseignant.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListeAbsencesAgent.this, "Erreur de chargement des détails de l'absence.", Toast.LENGTH_SHORT).show();
                });
    }


}
