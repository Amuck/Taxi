package com.bdstar.taxi.adapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bdstar.taxi.net.APIService;
import com.bumptech.glide.Glide;
import com.bdstar.taxi.R;

public class BindingAdapters {
    @BindingAdapter({"imageUrl"})
    public static void loadimage(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(APIService.IMG_URL + url)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
