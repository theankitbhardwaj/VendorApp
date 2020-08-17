package com.ganesh.vendorapp.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductView> {

    ArrayList<Products> productsList = new ArrayList<>();

    public ProductsAdapter(ArrayList<Products> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_products,parent,false);

        return new ProductView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {
        Products products = productsList.get(position);
        holder.tv_item_title.setText(products.getTitle());
        holder.tv_item_company.setText(products.getCompany());
        holder.tv_item_color_variant.setText(products.getVariants().size() + "");
        holder.tv_item_price.setText(products.getVariants().get(0).getPrice() + "");

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductView extends RecyclerView.ViewHolder{

        TextView tv_item_title, tv_item_company,tv_item_color_variant,tv_item_price;

        public ProductView(@NonNull View itemView) {
            super(itemView);

            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            tv_item_company = itemView.findViewById(R.id.tv_item_company);
            tv_item_color_variant = itemView.findViewById(R.id.tv_item_color_variant);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);

        }
    }
}
