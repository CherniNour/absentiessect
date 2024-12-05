package com.example.absentiessect1.Enseignant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListeAbsencesEnseignant extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterAbsenceEnseignant absenceAdapter;
    private List<Absence> absencesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_absences_enseignant);

        recyclerView = findViewById(R.id.recycler_view_absences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        absencesList = new ArrayList<>();
        absenceAdapter = new AdapterAbsenceEnseignant(absencesList);
        recyclerView.setAdapter(absenceAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve the teacher's name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        String teacherName = sharedPreferences.getString("name", null);

        if (teacherName != null) {
            db.collection("absences")
                    .whereEqualTo("email", teacherName) // Match `teacherName` with `Enseignant`
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            List<Absence> absences = queryDocumentSnapshots.toObjects(Absence.class);
                            absencesList.clear();
                            absencesList.addAll(absences);
                            absenceAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("ListeAbsences", "No absences found for teacher: " + teacherName);
                            Toast.makeText(this, "No absences found for " + teacherName, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ListeAbsences", "Error fetching absences", e);
                        Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    });

        }
}}
