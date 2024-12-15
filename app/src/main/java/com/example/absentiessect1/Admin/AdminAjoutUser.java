package com.example.absentiessect1.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.absentiessect1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminAjoutUser extends AppCompatActivity {

    private EditText editTextName, editTextLastName, editTextPhone, editTextEmail, editTextPassword;
    private RadioGroup radioGroupRole;
    private Button buttonAddUser;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ajout_user);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find views
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        buttonAddUser = findViewById(R.id.buttonAddUser);

        // Set button listener
        buttonAddUser.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Retrieve selected role from RadioGroup
            int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
            if (selectedRoleId == -1) {
                Toast.makeText(AdminAjoutUser.this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRoleButton = findViewById(selectedRoleId);
            String role = selectedRoleButton.getText().toString().trim();

            // Check for empty fields
            if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(AdminAjoutUser.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            createUserInAuthAndFirestore(name, lastName, phone, email, password, role);
        });
    }

    private void createUserInAuthAndFirestore(String name, String lastName, String phone, String email, String password, String role) {
        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            // Update user profile
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name + " " + lastName)
                                    .build();
                            user.updateProfile(profileUpdates);

                            // Add user to Firestore
                            addUserToFirestore(user.getUid(), name, lastName, phone, email, role);
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(AdminAjoutUser.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("AdminPage", "Error creating user", task.getException());
                            Toast.makeText(AdminAjoutUser.this, "Error creating user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFirestore(String userId, String name, String lastName, String phone, String email, String role) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("lastName", lastName);
        userMap.put("phone", phone);
        userMap.put("email", email);
        userMap.put("role", role);

        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminAjoutUser.this, "User added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminPage", "Error adding user to Firestore", e);
                    Toast.makeText(AdminAjoutUser.this, "Error adding user to database", Toast.LENGTH_SHORT).show();
                });
    }
}