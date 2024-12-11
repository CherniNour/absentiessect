package com.example.absentiessect1.Enseignant;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;

import java.util.List;

public class AdapterAbsenceEnseignant extends RecyclerView.Adapter<AdapterAbsenceEnseignant.AbsenceViewHolder> {
    private List<Absence> absences;

    // Constructeur
    public AdapterAbsenceEnseignant(List<Absence> absences) {
        this.absences = absences;
    }

    @NonNull
    @Override

    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence_enseignant, parent, false);
        return new AbsenceViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absences.get(position);
        holder.textClasse.setText("Classe: " + absence.getClasse());
        holder.textDate.setText("Date: " + absence.getDate());
        holder.textHeure.setText("Heure: " + absence.getHeure());
        holder.textSalle.setText("Salle: " + absence.getSalle());
    }

    @Override
    public int getItemCount() {
        return absences.size();
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView textEnseignant, textClasse, textDate, textHeure, textSalle;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            textClasse = itemView.findViewById(R.id.text_classe);
            textDate = itemView.findViewById(R.id.text_date);
            textHeure = itemView.findViewById(R.id.text_heure);
            textSalle = itemView.findViewById(R.id.text_salle);
        }
    }
}




