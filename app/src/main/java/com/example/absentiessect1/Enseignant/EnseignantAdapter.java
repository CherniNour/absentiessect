package com.example.absentiessect1.Enseignant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.EnseignantAbsenceItem;
import com.example.absentiessect1.R;

import java.util.List;

public class EnseignantAdapter extends RecyclerView.Adapter<EnseignantAdapter.EnseignantViewHolder> {

    private List<EnseignantAbsenceItem> enseignantsList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EnseignantAbsenceItem enseignantItem);
    }

    public EnseignantAdapter(List<EnseignantAbsenceItem> enseignantsList, Context context, OnItemClickListener listener) {
        this.enseignantsList = enseignantsList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public EnseignantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_enseignant, parent, false);
        return new EnseignantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EnseignantViewHolder holder, int position) {
        EnseignantAbsenceItem enseignant = enseignantsList.get(position);
        holder.enseignantName.setText(enseignant.getEnseignantNom());
        holder.absenceCount.setText("Absences: " + enseignant.getAbsenceCount());
    }

    @Override
    public int getItemCount() {
        return enseignantsList.size();
    }

    public class EnseignantViewHolder extends RecyclerView.ViewHolder {
        TextView enseignantName, absenceCount;
        CardView cardView;

        public EnseignantViewHolder(View itemView) {
            super(itemView);
            enseignantName = itemView.findViewById(R.id.enseignant_name);
            absenceCount = itemView.findViewById(R.id.absence_count);
            cardView = itemView.findViewById(R.id.card_view);

            cardView.setOnClickListener(v -> listener.onItemClick(enseignantsList.get(getAdapterPosition())));
        }
    }
}

