package com.example.farmermarketplace.customer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.models.SearchResult;

import java.util.List;

// Each row IS a farmer selling the searched product - name, price, distance and location
// are all shown directly, with no intermediate "product page" tap required.
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private final List<SearchResult> results;

    public SearchResultsAdapter(List<SearchResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult r = results.get(position);
        holder.farmerName.setText(r.farmer.name + (r.farmer.farmName != null ? " · " + r.farmer.farmName : ""));
        holder.priceText.setText(String.format("₹%.2f / %s", r.price, r.unit));
        holder.quantityText.setText("Available: " + r.quantityAvailable + " " + r.unit);
        holder.addressText.setText(r.farmer.address != null ? r.farmer.address : "Location not set");
        holder.distanceText.setText(r.distanceKm != null ? String.format("%.1f km away", r.distanceKm) : "");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FarmerProfileActivity.class);
            intent.putExtra("farmerId", r.farmer.id);
            intent.putExtra("farmerPhone", r.farmer.phone);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return results.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView farmerName, priceText, quantityText, addressText, distanceText;
        ViewHolder(View itemView) {
            super(itemView);
            farmerName = itemView.findViewById(R.id.farmerName);
            priceText = itemView.findViewById(R.id.priceText);
            quantityText = itemView.findViewById(R.id.quantityText);
            addressText = itemView.findViewById(R.id.addressText);
            distanceText = itemView.findViewById(R.id.distanceText);
        }
    }
}
