package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;
import com.smarteist.autoimageslider.SliderView;

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
        ImageSliderAdapter imageSliderAdapter;
        holder.title.setText(variantsItems.get(position).getVariantName());
        holder.price.setText("Each unit price is - " + variantsItems.get(position).getPrice());
        holder.quantity.setText("Total Quantity is - " + variantsItems.get(position).getQuantity());
        imageSliderAdapter = new ImageSliderAdapter(context, variantsItems.get(position).getImage());
        holder.imageSlider.setSliderAdapter(imageSliderAdapter);
        if (position % 2 != 0)
            holder.card.setCardBackgroundColor(Color.rgb(237, 237, 237));
        else
            holder.card.setCardBackgroundColor(Color.rgb(255, 255, 255));

        holder.remove.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return variantsItems.size();
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
