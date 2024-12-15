package com.example.absentiessect1.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absentiessect1.Models.Enseignant;
import com.example.absentiessect1.R;
import java.util.List;

public class EnseignantAdapter extends RecyclerView.Adapter<EnseignantAdapter.EnseignantViewHolder> {

    private List<Enseignant> enseignantList;
    private OnItemClickListener listener;

    public EnseignantAdapter(List<Enseignant> enseignantList, OnItemClickListener listener) {
        this.enseignantList = enseignantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EnseignantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enseignant_admin, parent, false);
        return new EnseignantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnseignantViewHolder holder, int position) {
        Enseignant enseignant = enseignantList.get(position);
        holder.enseignantName.setText(enseignant.getNom());
        holder.enseignantDetails.setText(enseignant.getDetails());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(enseignant));

        // Set click listener for the "Supprimer" button
        holder.deleteButton.setOnClickListener(v -> {
            if (holder.itemView.getContext() instanceof ListeEnseignantAdmin) {
                ((ListeEnseignantAdmin) holder.itemView.getContext()).deleteEnseignant(enseignant.getEmail());
                enseignantList.remove(position);
                notifyItemRemoved(position); // Remove the enseignant from the list
            }
        });

    }

    @Override
    public int getItemCount() {
        return enseignantList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Enseignant enseignant);
    }

    public static class EnseignantViewHolder extends RecyclerView.ViewHolder {

        TextView enseignantName, enseignantDetails;
        Button deleteButton;

        public EnseignantViewHolder(View itemView) {
            super(itemView);
            enseignantName = itemView.findViewById(R.id.enseignant_name);
            enseignantDetails = itemView.findViewById(R.id.enseignant_details);
            deleteButton = itemView.findViewById(R.id.supprimer_button);
        }
    }
}