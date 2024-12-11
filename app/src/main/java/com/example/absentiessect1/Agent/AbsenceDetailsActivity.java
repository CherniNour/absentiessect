package com.example.absentiessect1.Agent;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AbsenceAdapter absenceAdapter;
    private List<Absence> absenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_details); // Ensure this layout is defined correctly

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_absences); // ID must match your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the Absence list passed from the previous activity
        absenceList = (List<Absence>) getIntent().getSerializableExtra("absenceDetailsList");

        if (absenceList != null && !absenceList.isEmpty()) {
            // Set the adapter with the absence list
            absenceAdapter = new AbsenceAdapter(absenceList);
            recyclerView.setAdapter(absenceAdapter);
        } else {
            // If no data is passed, show a message
            Toast.makeText(this, "No absences to display", Toast.LENGTH_SHORT).show();
        }
    }
}
