package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.activities.AddProductActivity;
import com.ganesh.vendorapp.models.Variants;

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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_add_variant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.delete.setOnClickListener(view -> {
            removeAt(position);
        });

        holder.addImage.setOnClickListener(view -> {
            ((AddProductActivity) context).getImage(position);
        });


        holder.close1.setOnClickListener(view -> {
            variantsList.get(position).getImage().remove(0);
            holder.image1.setVisibility(View.GONE);
            holder.close1.setVisibility(View.GONE);
        });
        holder.close2.setOnClickListener(view -> {
            variantsList.get(position).getImage().remove(1);
            holder.image2.setVisibility(View.GONE);
            holder.close2.setVisibility(View.GONE);
        });
        holder.close3.setOnClickListener(view -> {
            variantsList.get(position).getImage().remove(2);
            holder.image3.setVisibility(View.GONE);
            holder.close3.setVisibility(View.GONE);
        });
        holder.close4.setOnClickListener(view -> {
            variantsList.get(position).getImage().remove(3);
            holder.image4.setVisibility(View.GONE);
            holder.close4.setVisibility(View.GONE);
        });
        holder.close5.setOnClickListener(view -> {
            variantsList.get(position).getImage().remove(4);
            holder.image5.setVisibility(View.GONE);
            holder.close5.setVisibility(View.GONE);
        });


        if (variantsList.get(position).getImage() == null) {
            holder.image1.setVisibility(View.GONE);
            holder.image2.setVisibility(View.GONE);
            holder.image3.setVisibility(View.GONE);
            holder.image4.setVisibility(View.GONE);
            holder.image5.setVisibility(View.GONE);

            holder.close1.setVisibility(View.GONE);
            holder.close2.setVisibility(View.GONE);
            holder.close3.setVisibility(View.GONE);
            holder.close4.setVisibility(View.GONE);
            holder.close5.setVisibility(View.GONE);
        } else if (variantsList.get(position).getImage().size() == 1) {

            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.GONE);
            holder.image3.setVisibility(View.GONE);
            holder.image4.setVisibility(View.GONE);
            holder.image5.setVisibility(View.GONE);

            holder.close1.setVisibility(View.VISIBLE);
            holder.close2.setVisibility(View.GONE);
            holder.close3.setVisibility(View.GONE);
            holder.close4.setVisibility(View.GONE);
            holder.close5.setVisibility(View.GONE);

            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image1);
        } else if (variantsList.get(position).getImage().size() == 2) {

            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.image3.setVisibility(View.GONE);
            holder.image4.setVisibility(View.GONE);
            holder.image5.setVisibility(View.GONE);

            holder.close1.setVisibility(View.VISIBLE);
            holder.close2.setVisibility(View.VISIBLE);
            holder.close3.setVisibility(View.GONE);
            holder.close4.setVisibility(View.GONE);
            holder.close5.setVisibility(View.GONE);

            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image1);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image2);
        } else if (variantsList.get(position).getImage().size() == 3) {

            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.image3.setVisibility(View.VISIBLE);
            holder.image4.setVisibility(View.GONE);
            holder.image5.setVisibility(View.GONE);

            holder.close1.setVisibility(View.VISIBLE);
            holder.close2.setVisibility(View.VISIBLE);
            holder.close3.setVisibility(View.VISIBLE);
            holder.close4.setVisibility(View.GONE);
            holder.close5.setVisibility(View.GONE);

            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image1);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image2);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image3);

        } else if (variantsList.get(position).getImage().size() == 4) {

            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.image3.setVisibility(View.VISIBLE);
            holder.image4.setVisibility(View.VISIBLE);
            holder.image5.setVisibility(View.GONE);

            holder.close1.setVisibility(View.VISIBLE);
            holder.close2.setVisibility(View.VISIBLE);
            holder.close3.setVisibility(View.VISIBLE);
            holder.close4.setVisibility(View.VISIBLE);
            holder.close5.setVisibility(View.GONE);

            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image1);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image2);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image3);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(3))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image4);

        } else if (variantsList.get(position).getImage().size() == 5) {

            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.image3.setVisibility(View.VISIBLE);
            holder.image4.setVisibility(View.VISIBLE);
            holder.image5.setVisibility(View.VISIBLE);

            holder.close1.setVisibility(View.VISIBLE);
            holder.close2.setVisibility(View.VISIBLE);
            holder.close3.setVisibility(View.VISIBLE);
            holder.close4.setVisibility(View.VISIBLE);
            holder.close5.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image1);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image2);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image3);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(3))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image4);
            Glide.with(context)
                    .load(variantsList.get(position).getImage().get(4))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.image5);
        }

    }
    public void removeAt(int position) {
        variantsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, variantsList.size());
    }

    public List<Variants> getArrayList() {
        return variantsList;
    }

    @Override
    public int getItemCount() {
        return variantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText variantName, variantQuantity, variantPrice;
        private ImageView image1, image2, image3, image4, image5;
        private ImageButton close1, close2, close3, close4, close5, delete, addImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            variantName = itemView.findViewById(R.id.et_variant_name);
            variantQuantity = itemView.findViewById(R.id.et_variant_quantity);
            variantPrice = itemView.findViewById(R.id.et_variant_price);
            delete = itemView.findViewById(R.id.close_imageButton);
            addImage = itemView.findViewById(R.id.add_variant_image);

            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            image5 = itemView.findViewById(R.id.image5);

            close1 = itemView.findViewById(R.id.close1);
            close2 = itemView.findViewById(R.id.close2);
            close3 = itemView.findViewById(R.id.close3);
            close4 = itemView.findViewById(R.id.close4);
            close5 = itemView.findViewById(R.id.close5);


            variantName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Variants variants = variantsList.get(getAdapterPosition());
                    variants.setVariant_name(charSequence + "");
                    variantsList.set(getAdapterPosition(), variants);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            variantPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Variants variants = variantsList.get(getAdapterPosition());
                    String price = charSequence + "";
                    if (!price.equals("")) {
                        variants.setPrice(Double.parseDouble(price));
                    } else {
                        variants.setPrice(0);
                    }
                    variantsList.set(getAdapterPosition(), variants);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            variantQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Variants variants = variantsList.get(getAdapterPosition());
                    String quantity = charSequence + "";
                    if (!quantity.equals("")) {
                        variants.setQuantity(Integer.parseInt(quantity));
                    } else {
                        variants.setQuantity(0);
                    }
                    variantsList.set(getAdapterPosition(), variants);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
    }
}
