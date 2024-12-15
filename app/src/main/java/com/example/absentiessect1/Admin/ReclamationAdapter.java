package com.example.absentiessect1.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absentiessect1.Models.Reclamation;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ReclamationAdapter extends RecyclerView.Adapter<ReclamationAdapter.ReclamationViewHolder> {

    private List<Reclamation> reclamationList;
    private FirebaseFirestore db;
    private Context context;

    // Constructor for the adapter
    public ReclamationAdapter(Context context, List<Reclamation> reclamationList) {
        this.context = context;
        this.reclamationList = reclamationList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReclamationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reclamation, parent, false);
        return new ReclamationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamationViewHolder holder, int position) {
        Reclamation reclamation = reclamationList.get(position);

        // Displaying reclamation data
        holder.subjectText.setText(reclamation.getSubject());
        holder.messageText.setText(reclamation.getMessage());

        // Get the teacher's name based on the enseignantId
        String enseignantId = reclamation.getEnseignantId();

        // Fetch the teacher's name from Firestore
        db.collection("users").document(enseignantId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String name = document.getString("name");
                        String lastName = document.getString("lastName");
                        holder.teacherNameText.setText(name + " " + lastName);
                    } else {
                        holder.teacherNameText.setText("Unknown Teacher");
                    }
                });

        // Display the current status of the reclamation
        holder.statusText.setText(reclamation.getEtat());

        // Enable or disable the approve/reject buttons based on the current status
        if (reclamation.getEtat().equals("en attente")) {
            holder.approveButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);
        } else {
            holder.approveButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
        }

        // Approve button click handler
        holder.approveButton.setOnClickListener(v -> {
            // Update Firestore to "Approuvée"
            db.collection("reclamations").document(reclamation.getId())
                    .update("etat", "approuvée")
                    .addOnSuccessListener(aVoid -> {
                        // Update UI status instantly
                        reclamation.setEtat("approuvée");
                        notifyItemChanged(position);
                    });
        });

        // Reject button click handler
        holder.rejectButton.setOnClickListener(v -> {
            // Update Firestore to "Rejettée"
            db.collection("reclamations").document(reclamation.getId())
                    .update("etat", "rejettée")
                    .addOnSuccessListener(aVoid -> {
                        // Update UI status instantly
                        reclamation.setEtat("rejettée");
                        notifyItemChanged(position);
                    });
        });
    }

    @Override
    public int getItemCount() {
        return reclamationList.size();
    }

    public static class ReclamationViewHolder extends RecyclerView.ViewHolder {

        TextView teacherNameText, subjectText, messageText, statusText;
        Button approveButton, rejectButton;

        public ReclamationViewHolder(View itemView) {
            super(itemView);
            teacherNameText = itemView.findViewById(R.id.teacher_name);
            subjectText = itemView.findViewById(R.id.reclamation_subject);
            messageText = itemView.findViewById(R.id.reclamation_message);
            statusText = itemView.findViewById(R.id.reclamation_status);
            approveButton = itemView.findViewById(R.id.btn_approve);
            rejectButton = itemView.findViewById(R.id.btn_reject);
        }
    }
}