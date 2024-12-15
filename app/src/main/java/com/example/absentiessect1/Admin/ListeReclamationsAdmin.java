package com.example.absentiessect1.Admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absentiessect1.Models.Reclamation;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListeReclamationsAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Reclamation> reclamationList;
    private FirebaseFirestore db;
    private ReclamationAdapter reclamationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_reclamations_admin);

        recyclerView = findViewById(R.id.recyclerViewReclamations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        reclamationList = new ArrayList<>();

        fetchReclamations();
    }

    private void fetchReclamations() {
        // Fetch all reclamations (including those with 'approuvée' or 'rejettée' statuses)
        db.collection("reclamations")
                .get()  // No filter, fetch all reclamations
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reclamation reclamation = document.toObject(Reclamation.class);
                            reclamation.setId(document.getId());  // Save the document ID
                            reclamationList.add(reclamation);
                        }

                        // Set the adapter
                        reclamationAdapter = new ReclamationAdapter(ListeReclamationsAdmin.this, reclamationList);
                        recyclerView.setAdapter(reclamationAdapter);
                    } else {
                        // Handle failure
                    }
                });
    }
}