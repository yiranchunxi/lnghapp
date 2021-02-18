package com.siasun.rtd.lngh.ui.pager;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.siasun.rtd.lngh.R;

public class GuidePagerAdapter extends PagerAdapter {

    private static final int[] DRAWABLES = {R.drawable.guide_01, R.drawable.guide_02,R.drawable.guide_03,R.drawable.guide_04};

    @Override
    public int getCount() {
        return DRAWABLES.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AppCompatImageView imageView = new AppCompatImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(DRAWABLES[position]);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
