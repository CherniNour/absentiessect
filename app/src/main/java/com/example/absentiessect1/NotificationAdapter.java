package com.example.absentiessect1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.dateTextView.setText(notification.getDate());

        // Afficher l'état de la notification (Lue ou Non lue)
        if (notification.isRead()) {
            holder.statusTextView.setText("Lue");
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.statusTextView.setText("Non lue");
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        // Mark as read when clicked
        holder.itemView.setOnClickListener(v -> {
            notification.setRead(true);  // Marquer comme lue
            notifyItemChanged(position); // Notifier que l'élément a changé
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, messageTextView, dateTextView, statusTextView;

        public NotificationViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.notificationTitle);
            messageTextView = view.findViewById(R.id.notificationMessage);
            dateTextView = view.findViewById(R.id.notificationDate);
            statusTextView = view.findViewById(R.id.notificationStatus);  // Référence du TextView pour le statut
        }
    }
}
