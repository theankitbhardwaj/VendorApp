package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.ProductsItem;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductView> {

    public AsyncListDiffer<ProductsItem> differ;

    public ProductsAdapter() {
        differ = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<ProductsItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull ProductsItem oldItem, @NonNull ProductsItem newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ProductsItem oldItem, @NonNull ProductsItem newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }
        });
    }

    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_products, parent, false);
        return new ProductView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {
        ProductsItem products = differ.getCurrentList().get(position);
        holder.tv_item_title.setText(products.getTitle());
        holder.tv_item_company.setText(products.getCompany());
        holder.tv_item_color_variant.setText(products.getVariants().size() + "");
        holder.tv_item_price.setText(products.getVariants().get(0).getPrice() + "");

        Glide.with(holder.context)
                .load(products.getVariants().get(0).getImage())
                .into(holder.view);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class ProductView extends RecyclerView.ViewHolder {

        TextView tv_item_title, tv_item_company, tv_item_color_variant, tv_item_price;
        ImageView view;
        Context context;

        public ProductView(@NonNull View itemView) {
            super(itemView);

            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            tv_item_company = itemView.findViewById(R.id.tv_item_company);
            tv_item_color_variant = itemView.findViewById(R.id.tv_item_color_variant);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);
            view = itemView.findViewById(R.id.imageView6);
            context = itemView.getContext();

        }
    }
}
