package com.example.absentiessect1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absentiessect1.Models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(List<Notification> notifications, OnNotificationClickListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationMessage.setText(notification.getMessage());

        // Set the status to "Lue" if read, "Non Lue" otherwise
        holder.notificationStatus.setText(notification.isRead() ? "Lue" : "Non Lue");

        // Set click listener to mark as read
        holder.itemView.setOnClickListener(v -> listener.onNotificationClick(notification));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notificationMessage;

        TextView notificationStatus;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            notificationMessage = itemView.findViewById(R.id.notificationMessage);
            notificationStatus = itemView.findViewById(R.id.notificationStatus);
        }
    }
}
