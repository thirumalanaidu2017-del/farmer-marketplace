package com.example.farmermarketplace.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.models.Farmer;

import java.util.List;

public class PendingFarmersAdapter extends RecyclerView.Adapter<PendingFarmersAdapter.ViewHolder> {

    public interface OnApproveListener { void onApprove(Farmer farmer, int position); }

    private final List<Farmer> farmers;
    private final OnApproveListener listener;

    public PendingFarmersAdapter(List<Farmer> farmers, OnApproveListener listener) {
        this.farmers = farmers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_farmer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Farmer f = farmers.get(position);
        holder.name.setText(f.user != null ? f.user.name : "Unknown");
        holder.email.setText(f.user != null ? f.user.email : "");
        holder.address.setText(f.address != null ? f.address : "No address set");
        holder.approveButton.setOnClickListener(v -> listener.onApprove(f, position));
    }

    @Override
    public int getItemCount() { return farmers.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, address;
        Button approveButton;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pendingFarmerName);
            email = itemView.findViewById(R.id.pendingFarmerEmail);
            address = itemView.findViewById(R.id.pendingFarmerAddress);
            approveButton = itemView.findViewById(R.id.approveButton);
        }
    }
}
