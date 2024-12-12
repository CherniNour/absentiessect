package com.example.absentiessect1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.absentiessect1.Login;
import com.example.absentiessect1.R;

public class DashboardAdmin extends AppCompatActivity {

    private CardView dashboardCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_admin);

        // Initialize only the dashboardCard view
        dashboardCard = findViewById(R.id.dashboard); // Make sure the ID matches with the XML

        // Check if the dashboardCard is initialized and set the onClick listener
        if (dashboardCard != null) {
            dashboardCard.setOnClickListener(v -> navigateToActivity(Statistics.class)); // Replace with the desired activity
        } else {
            Log.e("DashboardAdmin", "dashboardCard is null");
        }
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
}
