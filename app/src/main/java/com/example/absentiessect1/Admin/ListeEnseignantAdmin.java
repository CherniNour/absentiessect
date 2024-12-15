package com.example.absentiessect1.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absentiessect1.Models.Enseignant;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListeEnseignantAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Enseignant> enseignantList;
    private EnseignantAdapter enseignantAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_enseignant_admin);

        recyclerView = findViewById(R.id.recyclerViewEnseignant);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        enseignantList = new ArrayList<>();
        enseignantAdapter = new EnseignantAdapter(enseignantList, new EnseignantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Enseignant enseignant) {
                // Handle item click if needed
            }
        });
        recyclerView.setAdapter(enseignantAdapter);

        db = FirebaseFirestore.getInstance();
        fetchEnseignants();
    }

    private void fetchEnseignants() {
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("role", "Enseignant");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                enseignantList.clear(); // Clear the list before adding new data
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nom = document.getString("name");
                    String lastName = document.getString("lastName");
                    String phone = document.getString("phone");
                    String email = document.getString("email");
                    String details = "Nom: " + nom + "\nLast Name: " + lastName + "\nPhone: " + phone + "\nEmail: " + email;

                    Enseignant enseignant = new Enseignant(nom, details, email); // Pass email here
                    enseignantList.add(enseignant);
                }
                enseignantAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ListeEnseignantAdmin.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteEnseignant(String email) {
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.collection("users").document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ListeEnseignantAdmin.this, "Enseignant deleted", Toast.LENGTH_SHORT).show();
                                // Refresh the list after deletion
                                fetchEnseignants();
                            })
                            .addOnFailureListener(e -> Toast.makeText(ListeEnseignantAdmin.this, "Error deleting enseignant", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}