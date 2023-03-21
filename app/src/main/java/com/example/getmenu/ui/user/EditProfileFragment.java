package com.example.getmenu.ui.user;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.User;
import com.example.getmenu.R;
import com.example.getmenu.Utils;
import com.example.getmenu.databinding.FragmentEditProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class EditProfileFragment extends Fragment {

    User createdUser;
    FragmentEditProfileBinding binding;
    View view;
    ActivityResultLauncher<Void> cameraLauncher;
    Uri imageUri;
    ActivityResultLauncher<String> galleryAppLauncher;
    TextView userName;
    TextView userEmail;
    ImageView userImage;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if(result != null){
                    binding.editprofileImg.setImageBitmap(result);
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
                    binding.editprofileImg.setImageURI(result);
                    imageUri = result;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        createdUser = EditProfileFragmentArgs.fromBundle(getArguments()).getUser();
        userName = binding.editprofileNamePt;
        userName.setText(createdUser.getName());
        userEmail = binding.editprofileEmailPt;
        userEmail.setText(createdUser.getEmail());
        userImage = binding.editprofileImg;
        Picasso.get().load(Uri.parse(createdUser.getProfileImageUrl())).into(userImage);
        progressBar = binding.editProfileProgressBar;
        progressBar.setVisibility(View.INVISIBLE);

        binding.editprofileCameraImgbtn.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        binding.editprofileGalleryImgbtn.setOnClickListener(view1 -> {
            galleryAppLauncher.launch("image/*");
        });

        binding.editprofileSaveBtn.setOnClickListener(view1 -> {
            if(userName.getText().toString().length() > 0 && userEmail.getText().toString().length() > 0){
                progressBar.setVisibility(View.VISIBLE);
                updateUserDetailsFromInput();
                binding.editprofileErrorMag.setText("");

                Model.instance().editUser(createdUser, imageUri, () -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Navigation.findNavController(view).navigate(R.id.action_editProfileFragment_to_profileFragment2);
                    Utils.print("success edit user profile: " + createdUser.getId());
                });

            }else{
                binding.editprofileErrorMag.setText("Please fill all fields");
            }

        });

        return view;
    }

    public void updateUserDetailsFromInput() {
        createdUser.setName(userName.getText().toString());
        createdUser.setEmail(userEmail.getText().toString());
    }
}