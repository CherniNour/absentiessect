package com.example.absentiessect1.Agent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Absence;
import com.example.absentiessect1.R;
import java.util.List;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private List<Absence> absenceList;

    public AbsenceAdapter(List<Absence> absenceList) {
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence_enseignant, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absenceList.get(position);
        holder.classeTextView.setText("Classe: " + absence.getClasse());
        holder.dateTextView.setText("Date: " + absence.getDate());
        holder.heureTextView.setText("Heure: " + absence.getHeure());
        holder.salleTextView.setText("Salle: " + absence.getSalle());
        holder.enseignantTextView.setText("Enseignant: " + absence.getEnseignantNom());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView classeTextView, dateTextView, heureTextView, salleTextView, enseignantTextView;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            classeTextView = itemView.findViewById(R.id.text_classe);
            dateTextView = itemView.findViewById(R.id.text_date);
            heureTextView = itemView.findViewById(R.id.text_heure);
            salleTextView = itemView.findViewById(R.id.text_salle);
            enseignantTextView = itemView.findViewById(R.id.text_enseignant);
        }
    }
}
