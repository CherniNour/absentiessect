package com.example.absentiessect1.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Agent;
import com.example.absentiessect1.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListeAgentAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AgentAdapter agentAdapter;
    private List<Agent> agentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_agent_admin);

        recyclerView = findViewById(R.id.recyclerViewAgents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        agentList = new ArrayList<>();
        agentAdapter = new AgentAdapter(agentList);
        recyclerView.setAdapter(agentAdapter);

        fetchAgents();
    }

    private void fetchAgents() {
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("role", "Agent")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    agentList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Boolean isEditable = document.getBoolean("isEditable");
                        if (isEditable == null) {
                            isEditable = false; // Default to false if the field is not found
                        }
                        Agent agent = new Agent(
                                document.getId(),
                                document.getString("name"),
                                document.getString("lastName"),
                                document.getString("phone"),
                                document.getString("email"),
                                isEditable
                        );
                        agentList.add(agent);
                    }
                    agentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ListeAgentAdmin.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // Adapter RecyclerView
    class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {
        private List<Agent> agentList;

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

            holder.nameEditText.setText(agent.getName());
            holder.lastNameEditText.setText(agent.getLastName());
            holder.phoneEditText.setText(agent.getPhone());
            holder.emailEditText.setText(agent.getEmail());

            holder.editButton.setOnClickListener(v -> {
                holder.nameEditText.setFocusableInTouchMode(true);
                holder.lastNameEditText.setFocusableInTouchMode(true);
                holder.phoneEditText.setFocusableInTouchMode(true);
                holder.emailEditText.setFocusableInTouchMode(true);
                holder.editButton.setVisibility(View.GONE);
                holder.confirmButton.setVisibility(View.VISIBLE);
            });

            holder.confirmButton.setOnClickListener(v -> {
                db.collection("users").document(agent.getId())
                        .update(
                                "name", holder.nameEditText.getText().toString(),
                                "lastName", holder.lastNameEditText.getText().toString(),
                                "phone", holder.phoneEditText.getText().toString(),
                                "email", holder.emailEditText.getText().toString()
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ListeAgentAdmin.this, "Agent mis à jour avec succès", Toast.LENGTH_SHORT).show();
                            holder.nameEditText.setFocusable(false);
                            holder.lastNameEditText.setFocusable(false);
                            holder.phoneEditText.setFocusable(false);
                            holder.emailEditText.setFocusable(false);
                            holder.editButton.setVisibility(View.VISIBLE);
                            holder.confirmButton.setVisibility(View.GONE);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ListeAgentAdmin.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show()
                        );
            });

            holder.deleteButton.setOnClickListener(v -> {
                db.collection("users").document(agent.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ListeAgentAdmin.this, "Agent supprimé", Toast.LENGTH_SHORT).show();
                            agentList.remove(position);
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ListeAgentAdmin.this, "Erreur de suppression", Toast.LENGTH_SHORT).show()
                        );
            });
        }

        @Override
        public int getItemCount() {
            return agentList.size();
        }

        class AgentViewHolder extends RecyclerView.ViewHolder {
            EditText nameEditText, lastNameEditText, phoneEditText, emailEditText;
            Button editButton, confirmButton, deleteButton;

            public AgentViewHolder(@NonNull View itemView) {
                super(itemView);
                nameEditText = itemView.findViewById(R.id.tvName);
                lastNameEditText = itemView.findViewById(R.id.tvLastName);
                phoneEditText = itemView.findViewById(R.id.tvPhone);
                emailEditText = itemView.findViewById(R.id.tvEmail);
                editButton = itemView.findViewById(R.id.btnEdit);
                confirmButton = itemView.findViewById(R.id.btnConfirm);

                deleteButton = itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}