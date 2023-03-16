package com.example.getmenu.Model;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.getmenu.MyApplication;
import com.example.getmenu.Utils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();
    private FireBaseModel fireBaseModel = new FireBaseModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    AppLocalDbRepository localDb = AppLocalDb.db;
    MutableLiveData<List<Post>> allPosts = new MutableLiveData<List<Post>>();

    public enum PostListLoadingState {
        loading, loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<>();

    public static Model instance(){
        return _instance;
    }

    private Model(){
    }
    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface AddUserListener {
        void onComplete();
    }
    public LiveData<List<Post>> getAllPosts() {
        refreshPostsList();
        return allPosts;
    }

    public void refreshPostsList() {

        //************************************************************************
        // Delete share preferences only dor debug!!!
        MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit().clear().commit();
        //************************************************************************

        Utils.print("Refreshing post list");
        postListLoadingState.setValue(PostListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(Post.POST_LAST_UPDATE, 0);

        // firebase - get all updates since lastlocalupdate
        FireBaseModel.getAllPosts(lastUpdateDate, data -> {
            Utils.print("Firebase return post list in size: " + data.size());

            // add all records to the local db
            executor.execute(() -> {
                Long localUpdateDate = new Long(0);
                for (Post post : data) {
                    // updating the deleted posts in room DB
                    if (post.isDeleted) {
                        AppLocalDb.db.postDao().deleteById(post.getId());
                    } else {
                        // if post already exist we can just insert (on conflict = replace)
                        AppLocalDb.db.postDao().insertAll(post);
                    }

                    if (localUpdateDate < post.getUpdateDate()) {
                        localUpdateDate = post.getUpdateDate();
                    }
                }


                // update last local update date
                MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit().putLong(Post.POST_LAST_UPDATE, localUpdateDate).commit();

                // return all data to caller
                // using post value will automatically send the variable to the main thread
                // because this is a live data
                List<Post> postList = AppLocalDb.db.postDao().getAllPosts();
                allPosts.postValue(postList);
                postListLoadingState.postValue(PostListLoadingState.loaded);
            });
        });
    }



    public interface AddPostListener{
        void onComplete();
    }

//    public Post getPost(int i){
//        return data.get(i);
//    }

    public void addPost(Post post , Uri imageUri, AddPostListener listener){
        executor.execute(() -> {
            FireBaseModel.addPost(post, imageUri, listener);
        });
//        executor.execute(()->{
//            localDb.postDao().insertAll(post);
//            mainHandler.post(()->listener.onComplete());
//        });
    }

    public void addUser(User user, Uri imageUri, AddUserListener addUserListener) {
        executor.execute(() -> fireBaseModel.addUser(user, imageUri, addUserListener));
    }

//    public void removeStudent(Post post){
//        data.remove(post);
//    }




}
