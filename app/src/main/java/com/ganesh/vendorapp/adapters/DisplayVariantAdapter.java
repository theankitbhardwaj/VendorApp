package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;

import java.util.List;

public class DisplayVariantAdapter extends RecyclerView.Adapter<DisplayVariantAdapter.ViewHolder> {

    List<VariantsItem> variantsItems;
    Context context;

    public DisplayVariantAdapter(List<VariantsItem> variantsItems, Context context) {
        this.variantsItems = variantsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.display_variant_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(variantsItems.get(position).getVariantName());
        holder.price.setText("Each unit price is - " + variantsItems.get(position).getPrice());
        holder.quantity.setText("Total Quantity is - " + variantsItems.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return variantsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, price, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.variantName);
            price = itemView.findViewById(R.id.variantPrice);
            quantity = itemView.findViewById(R.id.variantQuantity);

        }
    }
}
