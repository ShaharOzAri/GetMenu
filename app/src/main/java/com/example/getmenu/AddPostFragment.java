package com.example.getmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;

public class AddPostFragment extends Fragment {

    FragmentAddPostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    Uri imageUri;

    ActivityResultLauncher<String> galleryAppLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if(result != null){
                    binding.addPostAvatarImg.setImageBitmap(result);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    result.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), result, "Title", null);
                    imageUri =  Uri.parse(path);
                }
            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.addPostAvatarImg.setImageURI(result);
                    imageUri = result;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater,container,false);
        View view =binding.getRoot();

        binding.addPostSaveBtn.setOnClickListener(view1 -> {
            Post post= new Post();

            String title = binding.addPostTitlePt.getText().toString();
            post.setTitle(title);

            post.setUserId(MyApplication.user.getId());
            post.setUserName(MyApplication.user.getName());
            post.setUserProfileUrl(MyApplication.user.getProfileImageUrl());

            Model.instance().addPost(post, imageUri,()->{
                Navigation.findNavController(view1).popBackStack();
            });
        });

        binding.addPostCancelBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack());

        binding.addPostCameraImgbtn.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        binding.addPostGalleryImgbtn.setOnClickListener(view1 -> {
            galleryAppLauncher.launch("image/*");
        });

        return view;
    }


}