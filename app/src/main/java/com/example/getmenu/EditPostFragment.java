package com.example.getmenu;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmenu.Model.FireBaseModel;
import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.databinding.FragmentEditPostBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditPostFragment extends Fragment {

    Post createdPost;
    ProgressBar progressBar;
    EditText postDescription;
    TextView postTitle;
    TextView postAvgPrice;
    TextView titleError;
    TextView contentError;
    FragmentEditPostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    Uri imageUri;
    ActivityResultLauncher<String> galleryAppLauncher;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if(result != null){
                    binding.editpostImg.setImageBitmap(result);
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
                    binding.editpostImg.setImageURI(result);
                    imageUri = result;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditPostBinding.inflate(inflater,container,false);
        view = binding.getRoot();


        //set all variables
        createdPost = EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        postTitle = binding.editpostTitlePt;
        postTitle.setText(createdPost.getTitle());
        ImageView postImg = binding.editpostImg;
        Picasso.get().load(Uri.parse(createdPost.getPostImageUrl())).into(postImg);
        postDescription = binding.editpostDescriptionPt;
        postDescription.setText(createdPost.getDescription());
        postAvgPrice = binding.editpostAvgpricePt;
        postAvgPrice.setText(createdPost.getAvgPrice());

        binding.editpostCameraImgbtn.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        binding.editpostGalleryImgbtn.setOnClickListener(view1 -> {
            galleryAppLauncher.launch("image/*");
        });

        //on save btn click
        binding.editpostSaveBtn.setOnClickListener(view1 -> {
            if (validateUserInput()) {
                Post editedPost = createPostFromInput();

                editedPost.setTimestamp(createdPost.getTimestamp());
                editedPost.setId(createdPost.getId());
                editedPost.setPostImageUrl(createdPost.getPostImageUrl());
                editedPost.setImageVersion(createdPost.getImageVersion());

//                progressBar.setVisibility(View.VISIBLE);

                Model.instance().editPost(editedPost, imageUri, new FireBaseModel.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
//                        progressBar.setVisibility(View.INVISIBLE);
                        Navigation.findNavController(view).navigate(R.id.action_editPostFragment_to_nav_home);
                    }

                    @Override
                    public void onFail() {
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MyApplication.context, "failed to edit post", Toast.LENGTH_SHORT).show();
                        Utils.print("TAG: failed update post - " + editedPost.toString());
                    }
                });
            }
        });

        binding.editpostDeleteBtn.setOnClickListener(view1 -> {
            FireBaseModel.deletePost(createdPost, imageUri, new FireBaseModel.Listener() {
                @Override
                public void onComplete() {
                    Navigation.findNavController(view).navigate(R.id.action_editPostFragment_to_nav_home);
                }

                @Override
                public void onFail() {
                    Toast.makeText(MyApplication.context, "failed to delete post", Toast.LENGTH_SHORT).show();
                    Utils.print("TAG: failed delete post - " + createdPost.toString());
                }
            });
        });

        return view;
    }

    public boolean validateUserInput() {

        if (postTitle.getText() == null || postTitle.getText().equals("")) {
            titleError.setVisibility(View.VISIBLE);
            return false;
        }

        if (postDescription.getText() == null || postDescription.getText().equals("")) {
            contentError.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }

    public Post createPostFromInput() {
        Post post = new Post();

        post.setTitle(postTitle.getText().toString());
        post.setDescription(postDescription.getText().toString());
        post.setAvgPrice(postAvgPrice.getText().toString());
        post.setUserId(MyApplication.user.getId());
        post.setUserProfileUrl(MyApplication.user.getProfileImageUrl());
        post.setUserName(MyApplication.user.getName());

        return post;
    }

}