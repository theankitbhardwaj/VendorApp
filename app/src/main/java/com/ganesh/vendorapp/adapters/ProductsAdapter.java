package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.activities.ProductDetailActivity;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.ProductsItem;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductView> {

    private List<ProductsItem> productsItems;
    private List<ProductsItem> selectedItems;
    private Context context;

    public ProductsAdapter(List<ProductsItem> productsItems, Context context) {
        this.productsItems = productsItems;
        selectedItems = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_products, parent, false);
        return new ProductView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {
        ProductsItem product = productsItems.get(position);
        holder.tv_item_title.setText(product.getTitle());
//        holder.cardView.setCardBackgroundColor(product.isSelected() ? Color.CYAN : Color.WHITE);
        holder.tv_item_company.setText(product.getCompany());
        if (product.getVariants() != null) {
            holder.tv_item_color_variant.setText(product.getVariants().size() + "");
            holder.tv_item_price.setText(product.getVariants().get(0).getPrice() + "");

            Glide.with(context)
                    .load("https://sambalpurihaat.com/admin/images/vendor_product/" + product.getVariants().get(0).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.view);
        }

        holder.cardView.setOnClickListener(view -> {
            Intent i = new Intent(context, ProductDetailActivity.class);
            i.putExtra("product", product);
            context.startActivity(i);
        });

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedItems.add(product);
                holder.cardView.setCardBackgroundColor(Color.CYAN);
            } else {
                selectedItems.remove(product);
                holder.cardView.setCardBackgroundColor(Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public List<ProductsItem> getSelectedProducts() {
        if (selectedItems.isEmpty())
            return null;
        else
            return selectedItems;
    }

    public List<ProductsItem> getAdapterList() {
        return productsItems;
    }

    public class ProductView extends RecyclerView.ViewHolder {

        TextView tv_item_title, tv_item_company, tv_item_color_variant, tv_item_price;
        ImageView view;
        CardView cardView;
        CheckBox checkBox;

        public ProductView(@NonNull View itemView) {
            super(itemView);

            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            tv_item_company = itemView.findViewById(R.id.tv_item_company);
            tv_item_color_variant = itemView.findViewById(R.id.tv_item_color_variant);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);
            view = itemView.findViewById(R.id.imageView6);
            cardView = itemView.findViewById(R.id.card);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
