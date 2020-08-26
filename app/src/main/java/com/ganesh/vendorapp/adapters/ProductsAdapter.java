package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.ProductsItem;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductView> {

    private List<ProductsItem> productsItems;
    private Context context;

    public ProductsAdapter(List<ProductsItem> productsItems, Context context) {
        this.productsItems = productsItems;
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
        ProductsItem products = productsItems.get(position);
        holder.tv_item_title.setText(products.getTitle());
        holder.tv_item_company.setText(products.getCompany());
        if (products.getVariants() != null) {
            holder.tv_item_color_variant.setText(products.getVariants().size() + "");
            holder.tv_item_price.setText(products.getVariants().get(0).getPrice() + "");

            Glide.with(context)
                    .load("https://grras-apidashboard.000webhostapp.com/admin/images/vendor_product/" + products.getVariants().get(0).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.view);
        }
    }

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public class ProductView extends RecyclerView.ViewHolder {

        TextView tv_item_title, tv_item_company, tv_item_color_variant, tv_item_price;
        ImageView view;

        public ProductView(@NonNull View itemView) {
            super(itemView);

            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            tv_item_company = itemView.findViewById(R.id.tv_item_company);
            tv_item_color_variant = itemView.findViewById(R.id.tv_item_color_variant);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);
            view = itemView.findViewById(R.id.imageView6);

        }
    }
}
