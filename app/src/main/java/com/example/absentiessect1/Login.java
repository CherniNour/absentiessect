package com.example.absentiessect1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absentiessect1.Admin.DashboardAdmin;
import com.example.absentiessect1.Agent.DashboardAgent;
import com.example.absentiessect1.Enseignant.DashboardEnseignant;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // SharedPreferences file name
    private static final String PREF_NAME = "UserPref";

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
                        // Retrieve user details
                        String role = documentSnapshot.getString("role");
                        String email = documentSnapshot.getString("email");
                        String name = documentSnapshot.getString("name");
                        String lastName = documentSnapshot.getString("lastName");
                        String phone = documentSnapshot.getString("phone");

                        if (role != null && email != null) {
                            // Save user data in SharedPreferences
                            saveUserDataToPreferences(name, lastName, role, email, phone);

                            // If the user is a teacher, retrieve and save the FCM token
                            if (role.equalsIgnoreCase("enseignant")) {
                                getFCMTokenAndSave(userId);
                                navigateToTeacherPage();
                            } else if (role.equalsIgnoreCase("admin")) {
                                navigateToAdminPage();
                            } else if (role.equalsIgnoreCase("agent")) {
                                navigateToAgentPage();
                            } else {
                                Toast.makeText(Login.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Incomplete user data in the database", Toast.LENGTH_SHORT).show();
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

    // Retrieve and save the FCM token for the teacher
    private void getFCMTokenAndSave(String userId) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d("FCM", "FCM Token: " + token);

                        // Save the token in Firestore under the teacher's document
                        db.collection("users").document(userId)
                                .update("fcmToken", token)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FCM", "FCM token saved successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FCM", "Error saving FCM token", e);
                                });
                    } else {
                        Log.e("FCM", "Failed to retrieve FCM token", task.getException());
                    }
                });
    }

    private void saveUserDataToPreferences(String name, String lastName, String role, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("lastName", lastName);
        editor.putString("role", role);
        editor.putString("email", email);
        editor.putString("phone", phone);

        editor.apply(); // Save changes
        Log.d("Login", "User data saved to SharedPreferences");
    }

    private void navigateToAdminPage() {
        Intent intent = new Intent(this, DashboardAdmin.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTeacherPage() {
        Intent intent = new Intent(this, DashboardEnseignant.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAgentPage() {
        Intent intent = new Intent(this, DashboardAgent.class);
        startActivity(intent);
        finish();
    }
}
