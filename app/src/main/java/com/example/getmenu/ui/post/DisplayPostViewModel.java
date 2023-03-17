package com.example.getmenu.ui.post;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayPostViewModel extends ViewModel {
    public LiveData<List<Post>> liveDataPost;

    public DisplayPostViewModel() {
        liveDataPost = Model.instance().getAllPosts();
    }

    public LiveData<List<Post>> getData(String id) {
        if (id == null) {
            return liveDataPost;
        }

        List<Post> list = liveDataPost.getValue();
        List<Post> userListPost = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getUserId().equals(id)){
                userListPost.add(list.get(i));
            }
        }


        return new MutableLiveData<>(userListPost);
    }
}
