package com.example.absentiessect1.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Agent;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {

    private List<Agent> agentList; // List of agents
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Firestore instance

    public AgentAdapter(List<Agent> agentList) {
        this.agentList = agentList;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        Agent agent = agentList.get(position);

        // Populate fields with agent data
        holder.nameEditText.setText(agent.getName());
        holder.lastNameEditText.setText(agent.getLastName());
        holder.phoneEditText.setText(agent.getPhone());
        holder.emailEditText.setText(agent.getEmail());
        holder.confirmButton.setVisibility(View.GONE);

        // Modify Button: Enable editing
        holder.modifyButton.setOnClickListener(v -> {
            // Enable fields for editing
            holder.nameEditText.setFocusableInTouchMode(true);
            holder.lastNameEditText.setFocusableInTouchMode(true);
            holder.phoneEditText.setFocusableInTouchMode(true);
            holder.emailEditText.setFocusableInTouchMode(true);

            // Hide 'Modifier' button and show 'Confirmer'
            holder.modifyButton.setVisibility(View.GONE);
            holder.confirmButton.setVisibility(View.VISIBLE);
        });

        // Confirm Button: Update Firestore and reset view
        holder.confirmButton.setOnClickListener(v -> {
            db.collection("users").document(agent.getId())
                    .update(
                            "name", holder.nameEditText.getText().toString(),
                            "lastName", holder.lastNameEditText.getText().toString(),
                            "phone", holder.phoneEditText.getText().toString(),
                            "email", holder.emailEditText.getText().toString()
                    )
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Agent mis à jour", Toast.LENGTH_SHORT).show();                        // Disable fields after confirming
                        holder.nameEditText.setFocusable(false);
                        holder.lastNameEditText.setFocusable(false);
                        holder.phoneEditText.setFocusable(false);
                        holder.emailEditText.setFocusable(false);

                        // Show 'Modifier' button and hide 'Confirmer'
                        holder.confirmButton.setVisibility(View.GONE);
                        holder.modifyButton.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();                    });
        });

        // Delete Button: Remove agent from Firestore
        holder.deleteButton.setOnClickListener(v -> {
            db.collection("users").document(agent.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Agent supprimé", Toast.LENGTH_SHORT).show();                        agentList.remove(position);
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(), "Erreur de suppression", Toast.LENGTH_SHORT).show();                    });
        });
    }


    @Override
    public int getItemCount() {
        return agentList.size();
    }

    // Helper method to enable/disable fields
    private void setFieldsEnabled(AgentViewHolder holder, boolean enabled) {
        holder.nameEditText.setEnabled(enabled);
        holder.lastNameEditText.setEnabled(enabled);
        holder.phoneEditText.setEnabled(enabled);
        holder.emailEditText.setEnabled(enabled);
    }

    static class AgentViewHolder extends RecyclerView.ViewHolder {
        EditText nameEditText, lastNameEditText, phoneEditText, emailEditText;
        Button modifyButton, confirmButton, deleteButton;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.tvName);
            lastNameEditText = itemView.findViewById(R.id.tvLastName);
            phoneEditText = itemView.findViewById(R.id.tvPhone);
            emailEditText = itemView.findViewById(R.id.tvEmail);
            modifyButton = itemView.findViewById(R.id.btnEdit);
            confirmButton = itemView.findViewById(R.id.btnConfirm);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }
    }

}