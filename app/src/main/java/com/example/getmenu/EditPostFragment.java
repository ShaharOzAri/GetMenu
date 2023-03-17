package com.example.getmenu;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

public class EditPostFragment extends Fragment {

    Post createdPost;
    View mainView;
    Bitmap cameraBitmap;
    Button submitBtn;
    ProgressBar progressBar;
    EditText postDescription;
    Uri postImageURI = null;
    TextView postTitle;
    TextView titleError;
    TextView imageError;
    TextView contentError;
    ImageView postImageHolder;
    ImageView backgroundImageView;
    ImageButton cameraImageBtn;
    ImageButton galleryImageBtn;
    FragmentEditPostBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditPostBinding.inflate(inflater,container,false);
        view = binding.getRoot();


        //set all variables
        Post createdPost = EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        TextView name = view.findViewById(R.id.editpost_title_pt);
        name.setText(createdPost.getTitle());
        ImageView postImg = view.findViewById(R.id.editpost_img);
        Picasso.get().load(Uri.parse(createdPost.getPostImageUrl())).into(postImg);


        //on save btn click
        Button saveBtn = view.findViewById(R.id.editpost_save_btn);
        saveBtn.setOnClickListener(view1 -> {
            if (validateUserInput()) {
                Post editedPost = createPostFromInput();

                editedPost.setTimestamp(createdPost.getTimestamp());
                editedPost.setId(createdPost.getId());
                editedPost.setPostImageUrl(createdPost.getPostImageUrl());
                editedPost.setImageVersion(createdPost.getImageVersion());

//                progressBar.setVisibility(View.VISIBLE);

                Model.instance().editPost(editedPost, postImageURI, new FireBaseModel.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Navigation.findNavController(mainView).navigate(R.id.action_editPostFragment_to_nav_home);
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MyApplication.context, "failed to edit post", Toast.LENGTH_SHORT).show();
                        Utils.print("TAG: failed update post - " + editedPost.toString());
                    }
                });
            }
        });


        return view;

    }

    public boolean validateUserInput() {

//        if (postTitle.getText() == null || postTitle.getText().equals("")) {
//            titleError.setVisibility(View.VISIBLE);
//            return false;
//        }
//
//        if (postContent.getText() == null || postContent.getText().equals("")) {
//            contentError.setVisibility(View.VISIBLE);
//            return false;
//        }

        return true;
    }

    public Post createPostFromInput() {
        Post post = new Post();

        post.setTitle(postTitle.getText().toString());
        post.setDescription(postDescription.getText().toString());
        post.setUserId(MyApplication.user.getId());
        post.setUserProfileUrl(MyApplication.user.getProfileImageUrl());
        post.setUserName(MyApplication.user.getName());

        return post;
    }

}