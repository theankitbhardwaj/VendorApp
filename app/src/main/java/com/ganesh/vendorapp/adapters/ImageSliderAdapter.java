package com.ganesh.vendorapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends
        SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> images;

    public ImageSliderAdapter(Context context, List<String> images) {
        this.images = images;
        this.context = context;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        if (images.get(position).contains("content")) {
            Glide.with(viewHolder.itemView)
                    .load(images.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(viewHolder.sliderImage);
        } else if (images.get(position).contains("banners"))
            Glide.with(viewHolder.itemView)
                    .load(images.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(viewHolder.sliderImage);
        else
            Glide.with(viewHolder.itemView)
                    .load("https://sambalpurihaat.com/admin/images/vendor_product/" + images.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(viewHolder.sliderImage);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView sliderImage;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.sliderImage);
        }
    }

}
