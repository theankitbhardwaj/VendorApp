package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.activities.AddProductActivity;
import com.ganesh.vendorapp.models.Variants;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    List<Variants> variantsList;
    Context context;


    public VariantAdapter(List<Variants> variantsList, Context context) {
        this.variantsList = variantsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.display_variant_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageSliderAdapter imageSliderAdapter;
        Log.e("VariantAdapter", "onBindViewHolder: " + variantsList.get(position).toString());
        holder.title.setText(variantsList.get(position).getVariant_name());
        holder.price.setText("Each unit price is - " + variantsList.get(position).getPrice());
        holder.quantity.setText("Total Quantity is - " + variantsList.get(position).getQuantity());
        imageSliderAdapter = new ImageSliderAdapter(context, variantsList.get(position).getImage());
        holder.imageSlider.setSliderAdapter(imageSliderAdapter);
        if (position % 2 != 0)
            holder.card.setCardBackgroundColor(Color.rgb(237, 237, 237));
        else
            holder.card.setCardBackgroundColor(Color.rgb(255, 255, 255));

        holder.remove.setOnClickListener(view -> {
            removeAt(position);
        });
    }

    public void removeAt(int position) {
        variantsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, variantsList.size());
    }

    @Override
    public int getItemCount() {
        return variantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, quantity;
        private CardView card;
        private SliderView imageSlider;
        private ImageButton remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.variantName);
            price = itemView.findViewById(R.id.variantPrice);
            quantity = itemView.findViewById(R.id.variantQuantity);
            card = itemView.findViewById(R.id.card);
            imageSlider = itemView.findViewById(R.id.imageSlider);
            remove = itemView.findViewById(R.id.removeVariant);
        }
    }
}
