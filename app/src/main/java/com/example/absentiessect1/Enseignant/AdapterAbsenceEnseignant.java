package com.example.absentiessect1.Enseignant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Absence;
import com.example.absentiessect1.R;

import java.util.List;

public class AdapterAbsenceEnseignant extends RecyclerView.Adapter<AdapterAbsenceEnseignant.AbsenceViewHolder> {

    private List<Absence> absencesList;

    // Constructor
    public AdapterAbsenceEnseignant(List<Absence> absencesList) {
        this.absencesList = absencesList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_absence_enseignant, parent, false); // Use a layout for individual items
        return new AbsenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absencesList.get(position);
        holder.classeText.setText(absence.getClasse());
        holder.dateText.setText(absence.getDate());
        holder.heureText.setText(absence.getHeure());
        holder.salleText.setText(absence.getSalle());
    }

    @Override
    public int getItemCount() {
        return absencesList.size();
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {

        TextView classeText, dateText, heureText, salleText;

        public AbsenceViewHolder(View view) {
            super(view);
            classeText = view.findViewById(R.id.text_classe);
            dateText = view.findViewById(R.id.text_date);
            heureText = view.findViewById(R.id.text_heure);
            salleText = view.findViewById(R.id.text_salle);
        }
    }
}
