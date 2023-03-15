package com.example.getmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.databinding.FragmentAddPostBinding;
import com.example.getmenu.ui.home.HomeFragment;

public class AddPostFragment extends Fragment {

    FragmentAddPostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if(result != null){
                    binding.addPostAvatarImg.setImageBitmap(result);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater,container,false);
        View view =binding.getRoot();

        binding.addPostSaveBtn.setOnClickListener(view1 -> {
            String title = binding.addPostTitlePt.getText().toString();
            Post post= new Post();
            post.setTitle(title);
            post.setId("1");
            Model.instance().addPost(post,()->{
                Navigation.findNavController(view1).popBackStack();
            });
        });

        binding.addPostCancelBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack());

        binding.addPostCameraImgbtn.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        binding.addPostGalleryImgbtn.setOnClickListener(view1 -> {

        });

        return view;
    }
}