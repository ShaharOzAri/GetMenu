package com.example.getmenu.Model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    AppLocalDbRepository localDb = AppLocalDb.db;

    public static Model instance(){
        return _instance;
    }

    private Model(){
    }
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener callback){
        executor.execute(()->{
            List<Post> data =  localDb.postDao().getAllPosts();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainHandler.post(()->{
                callback.onComplete(data);
            });
        });
    }

    public interface AddPostListener{
        void onComplete();
    }

//    public Post getPost(int i){
//        return data.get(i);
//    }

    public void addPost(Post post , AddPostListener listener){
        executor.execute(()->{
            localDb.postDao().insertAll(post);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainHandler.post(()->{
                listener.onComplete();
            });
        });
    }

//    public void removeStudent(Post post){
//        data.remove(post);
//    }




}
