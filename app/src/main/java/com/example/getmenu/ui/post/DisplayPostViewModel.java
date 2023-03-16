package com.example.getmenu.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;

import java.util.List;

public class DisplayPostViewModel extends ViewModel {
    public LiveData<List<Post>> liveDataPost;

    public DisplayPostViewModel() {
        liveDataPost = Model.instance().getAllPosts();
    }

    public LiveData<List<Post>> getData() {
        return liveDataPost;
    }
}
