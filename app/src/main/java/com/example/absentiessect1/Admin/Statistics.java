package com.example.absentiessect1.Admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.absentiessect1.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statistics extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView claimsSummaryTextView;
    private TextView claimsPieChartTitle;
    private TextView absencesTeacherBarChartTitle;
    private TextView absencesPeriodLineChartTitle;
    private TextView absencesClassBarChartTitle;
    private TextView rolesPieChartTitle;

    private PieChart claimsPieChart;
    private BarChart absencesTeacherBarChart;
    private LineChart absencesPeriodLineChart;
    private BarChart absencesClassBarChart;
    private PieChart rolesPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = FirebaseFirestore.getInstance();

        claimsSummaryTextView = findViewById(R.id.claimsSummaryTextView);
        claimsPieChart = findViewById(R.id.claimsPieChart);
        absencesTeacherBarChart = findViewById(R.id.absencesTeacherBarChart);
        absencesPeriodLineChart = findViewById(R.id.absencesPeriodLineChart);
        absencesClassBarChart = findViewById(R.id.absencesClassBarChart);
        rolesPieChart = findViewById(R.id.rolesPieChart);

        claimsPieChartTitle = findViewById(R.id.claimsPieChartTitle);
        absencesTeacherBarChartTitle = findViewById(R.id.absencesTeacherBarChartTitle);
        absencesPeriodLineChartTitle = findViewById(R.id.absencesPeriodLineChartTitle);
        absencesClassBarChartTitle = findViewById(R.id.absencesClassBarChartTitle);
        rolesPieChartTitle = findViewById(R.id.rolesPieChartTitle);

        fetchClaimsData();
        fetchAbsencesData();
        fetchRolesData(); // Mise à jour pour afficher les rôles avec un PieChart
    }

    private void fetchClaimsData() {
        db.collection("reclamations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int total = 0, approved = 0, rejected = 0, pending = 0;

                for (QueryDocumentSnapshot document : task.getResult()) {
                    total++;
                    String state = document.getString("etat");
                    if ("approuvée".equals(state)) approved++;
                    else if ("rejetée".equals(state)) rejected++;
                    else if ("en attente".equals(state)) pending++;
                }

                claimsSummaryTextView.setText("Total des réclamations : " + total);

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(approved, "Approuvée"));
                entries.add(new PieEntry(rejected, "Rejetée"));
                entries.add(new PieEntry(pending, "En attente"));

                PieDataSet dataSet = new PieDataSet(entries, "Répartition des réclamations");
                dataSet.setColors(new int[]{R.color.green, R.color.red, R.color.yellow}, this);
                PieData pieData = new PieData(dataSet);
                claimsPieChart.setData(pieData);
                claimsPieChart.invalidate();
            }
        });
    }

    private void fetchAbsencesData() {
        db.collection("Absences").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> absencesByTeacher = new HashMap<>();
                Map<String, Integer> absencesByClass = new HashMap<>();
                Map<String, Integer> absencesByPeriod = new HashMap<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String teacher = document.getString("Enseignant");
                    String className = document.getString("Classe");
                    String period = document.getString("Date"); // Supposons que 'Date' représente la période

                    absencesByTeacher.put(teacher, absencesByTeacher.getOrDefault(teacher, 0) + 1);
                    absencesByClass.put(className, absencesByClass.getOrDefault(className, 0) + 1);
                    absencesByPeriod.put(period, absencesByPeriod.getOrDefault(period, 0) + 1);
                }

                populateBarChart(absencesTeacherBarChart, absencesByTeacher, "Enseignant");
                populateBarChart(absencesClassBarChart, absencesByClass, "Classe");
                populateLineChart(absencesPeriodLineChart, absencesByPeriod);
            }
        });
    }

    private void fetchRolesData() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> rolesCount = new HashMap<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String role = document.getString("role"); // Champ représentant le rôle
                    if (role != null) {
                        rolesCount.put(role, rolesCount.getOrDefault(role, 0) + 1);
                    }
                }

                populatePieChart(rolesPieChart, rolesCount);
            } else {
                Log.e("FetchRolesData", "Error fetching roles: ", task.getException());
            }
        });
    }

    private void populatePieChart(PieChart chart, Map<String, Integer> data) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Distribution des Rôles");
        dataSet.setColors(new int[]{R.color.blue, R.color.green, R.color.orange, R.color.red}, this);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(R.color.white));

        PieData pieData = new PieData(dataSet);

        chart.setData(pieData);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setEntryLabelTextSize(12f);
        chart.setEntryLabelColor(getResources().getColor(R.color.black));
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(45f);
        chart.invalidate(); // Rafraîchir le graphique
    }

    private void populateBarChart(BarChart chart, Map<String, Integer> data, String label) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, label);
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate(); // Rafraîchir le graphique
    }

    private void populateLineChart(LineChart chart, Map<String, Integer> data) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new Entry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Période");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // Rafraîchir le graphique
    }
}
