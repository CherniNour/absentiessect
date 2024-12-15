package com.example.absentiessect1.Enseignant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListeAbsencesEnseignant extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterAbsenceEnseignant absenceAdapter;
    private List<Absence> absencesList;

    private static final String PREF_NAME = "UserPref"; // SharedPreferences file name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_absences_enseignant);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize list and adapter
        absencesList = new ArrayList<>();
        absenceAdapter = new AdapterAbsenceEnseignant(absencesList);

        // RecyclerView configuration
        recyclerView = findViewById(R.id.recycler_view_absences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(absenceAdapter);

        // Retrieve teacher's full name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String lastName = sharedPreferences.getString("lastName", null);

        if (name != null && lastName != null) {
            String teacherFullName = name + " " + lastName; // Concatenate first and last name

            // Load absences for the teacher
            loadAbsencesForTeacher(teacherFullName);
        } else {
            Toast.makeText(this, "Nom d'enseignant introuvable dans les préférences", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAbsencesForTeacher(String teacherFullName) {
        db.collection("Absences")
                .whereEqualTo("Enseignant", teacherFullName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<Absence> absences = queryDocumentSnapshots.toObjects(Absence.class);
                        absencesList.clear();
                        absencesList.addAll(absences);
                        absenceAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("ListeAbsences", "No absences found for teacher: " + teacherFullName);
                        Toast.makeText(this, "Aucune absence trouvée pour " + teacherFullName, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ListeAbsences", "Error fetching absences", e);
                    Toast.makeText(this, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
                });
    }
}
