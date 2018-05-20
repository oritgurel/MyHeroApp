package com.oritmalki.myheroapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.oritmalki.myheroapp.R;

public class ImageFragment extends Fragment {

    private static final String ARGS_IMAGE_URL = "args_image_url";
    private PhotoView photoView;
    private String imageUrl;

    public ImageFragment() {

    }

    public static ImageFragment getInstance(String imageUrl) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_IMAGE_URL, imageUrl);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imageUrl = getArguments().getString(ARGS_IMAGE_URL);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        photoView = view.findViewById(R.id.photo_view);
        Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(photoView);


        return view;
    }
}
