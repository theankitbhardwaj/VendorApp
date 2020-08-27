package com.ganesh.vendorapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Helper {

    public String getRandomID() {
        UUID uniqueKey = UUID.randomUUID();
        String id = uniqueKey.toString();
        return "PR" + id.substring(0, id.indexOf("-"));
    }

    public String base64String(Uri uri, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgByte, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> base64String(List<String> uri, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        List<String> base64 = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++) {
            Bitmap bitmap;
            if (uri.get(i).contains("content")) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(uri.get(i)));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    byte[] imgByte = byteArrayOutputStream.toByteArray();
                    base64.add(Base64.encodeToString(imgByte, Base64.DEFAULT));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                base64.add(uri.get(i));
            }
        }
        return base64;
    }

    public boolean validateVariantData(Variants variants) {
        if (variants != null) {
            if (variants.getVariant_name() == null)
                return false;
            else if (String.valueOf(variants.getQuantity()) == null)
                return false;
            else if (String.valueOf(variants.getPrice()) == null)
                return false;
            else return variants.getImage() != null;
        } else
            return false;
    }

    public boolean validateVariantData(VariantsItem variants) {
        if (variants != null) {
            if (variants.getVariantName() == null)
                return false;
            else if (String.valueOf(variants.getQuantity()) == null)
                return false;
            else if (String.valueOf(variants.getPrice()) == null)
                return false;
            else return variants.getImage() != null;
        } else
            return false;
    }


}
