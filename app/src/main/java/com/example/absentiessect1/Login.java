package com.example.absentiessect1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.absentiessect1.Admin.DashboardAdmin;
import com.example.absentiessect1.Agent.DashboardAgent;
import com.example.absentiessect1.Enseignant.DashboardEnseignant;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find views
        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        // Set login button listener
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            authenticateUser(email, password);
        });

    }

    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("Login", "Authentication successful for user: " + user.getUid());
                            checkUserRole(user.getUid());
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e("Login", "Authentication failed", e);
                        Toast.makeText(Login.this, "Authentication Failed: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String userId) {
        // Retrieve the role from Firestore
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if (role != null) {
                            // Redirect based on role
                            if (role.equalsIgnoreCase("admin")) {
                                navigateToAdminPage();
                            } else if (role.equalsIgnoreCase("enseignant")) {
                                navigateToTeacherPage();
                            }
                            else if (role.equalsIgnoreCase("agent")) {
                                navigateToAgentPage();
                            }
                            else {
                                Toast.makeText(Login.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Role not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Login", "Error getting user role", e);
                    Toast.makeText(Login.this, "Error retrieving role", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToAdminPage() {
        Intent intent = new Intent(this, DashboardAdmin.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTeacherPage() {
        Intent intent = new Intent(this, DashboardEnseignant.class); // Create TeacherPage activity
        startActivity(intent);
        finish();
    }

    private void navigateToAgentPage() {
        Intent intent = new Intent(this, DashboardAgent.class); // Create AgentPage activity
        startActivity(intent);
        finish();
    }
}
