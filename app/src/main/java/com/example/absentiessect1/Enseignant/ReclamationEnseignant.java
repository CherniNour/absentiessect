package com.example.absentiessect1.Enseignant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


import java.util.HashMap;
import java.util.Map;

public class ReclamationEnseignant extends AppCompatActivity {

    private EditText etSubject, etMessage;
    private Button btnSendReclamation;
    private TextView tvUserId; // Pour afficher l'ID de l'utilisateur connecté
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;

    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamation_enseignant);
        auth = FirebaseAuth.getInstance();

        // Initialiser Firebase Firestore
        db = FirebaseFirestore.getInstance();

        currentUser = auth.getCurrentUser(); // pour la récupération de l'ID de l'enseignant connecté

        if (currentUser == null) {
            // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            Toast.makeText(this, "Veuillez vous connecter pour envoyer une réclamation.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialiser les vues
        etSubject = findViewById(R.id.et_subject);
        etMessage = findViewById(R.id.et_message);
        btnSendReclamation = findViewById(R.id.btn_send_reclamation);
        tvUserId = findViewById(R.id.tv_user_id);

        // Afficher l'ID utilisateur dans la TextView
        String enseignantId = currentUser.getUid();
        tvUserId.setText("ID Utilisateur : " + enseignantId);

        // Action pour le bouton "Envoyer"
        btnSendReclamation.setOnClickListener(v -> sendReclamation(enseignantId));
    }

    private void sendReclamation(String enseignantId) {
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        // Validation des champs
        if (TextUtils.isEmpty(subject)) {
            etSubject.setError("Le sujet est obligatoire");
            return;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Le message est obligatoire");
            return;
        }


        // Préparer les données pour Firestore
        Map<String, Object> reclamation = new HashMap<>();
        reclamation.put("enseignantId", enseignantId);
        reclamation.put("subject", subject);
        reclamation.put("message", message);
        reclamation.put("timestamp", System.currentTimeMillis());
        reclamation.put("etat", "en attente");

        // Ajouter la réclamation à Firestore
        db.collection("reclamations")
                .add(reclamation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ReclamationEnseignant.this, "Réclamation envoyée avec succès.", Toast.LENGTH_SHORT).show();
                    // Réinitialiser les champs après succès
                    etSubject.setText("");
                    etMessage.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ReclamationEnseignant.this, "Erreur lors de l'envoi de la réclamation.", Toast.LENGTH_SHORT).show();
                    Log.e("ReclamationEnseignant", "Erreur lors de l'ajout : ", e);
                });
    }
}
