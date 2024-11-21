package com.example.absentiessect1.Enseignant;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Absence;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        // Fetch absences data from Firestore
        db.collection("absences")
                .whereEqualTo("IDagent", "teacherID")  // Replace "teacherID" with the actual teacher's ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Absence> absences = queryDocumentSnapshots.toObjects(Absence.class);
                        absencesList.clear();
                        absencesList.addAll(absences);
                        absenceAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ListeAbsencesEnseignant.this, "Failed to load data", Toast.LENGTH_SHORT).show());


    }
}
