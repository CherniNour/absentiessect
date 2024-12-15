package com.example.absentiessect1.Admin;

import com.example.absentiessect1.Admin.AdminAjoutUser;
import com.example.absentiessect1.Admin.ListeEnseignantAdmin;
import com.example.absentiessect1.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminGestionUsers extends AppCompatActivity {

    CardView btnListeEnseignants, btnListeAgents, btnAjouterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gestion_users);

        // Initialize the buttons
        btnListeEnseignants = findViewById(R.id.btnListeEnseignants);
        btnListeAgents = findViewById(R.id.btnListeAgents);
        btnAjouterUser = findViewById(R.id.btnAjouterUser);

        // Button Actions
        btnListeEnseignants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminGestionUsers.this, ListeEnseignantAdmin.class);
                startActivity(intent);
            }
        });

        btnListeAgents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminGestionUsers.this, ListeAgentAdmin.class);
                startActivity(intent);
            }
        });

        btnAjouterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminGestionUsers.this, AdminAjoutUser.class);
                startActivity(intent);
            }
        });
    }
}