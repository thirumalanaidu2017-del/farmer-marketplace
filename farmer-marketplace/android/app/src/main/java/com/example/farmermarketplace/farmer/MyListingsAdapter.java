package com.example.farmermarketplace.farmer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.models.Listing;

import java.util.List;

public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.ViewHolder> {

    private final List<Listing> listings;

    public MyListingsAdapter(List<Listing> listings) {
        this.listings = listings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listing l = listings.get(position);
        holder.productName.setText(l.product != null ? l.product.name : "Product");
        holder.priceQty.setText(String.format("₹%.2f/%s · Qty: %d", l.price, l.unit, l.quantityAvailable));
        holder.statusText.setText(l.active ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() { return listings.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, priceQty, statusText;
        ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.listingProductName);
            priceQty = itemView.findViewById(R.id.listingPriceQty);
            statusText = itemView.findViewById(R.id.listingStatus);
        }
    }
}
