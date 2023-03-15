package com.example.getmenu.Model;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();
    private FireBaseModel fireBaseModel = new FireBaseModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    AppLocalDbRepository localDb = AppLocalDb.db;

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
    public void getAllPosts(Listener callback){
        fireBaseModel.getAllPosts(new Long(5221) , callback);
//        executor.execute(()->{
//            List<Post> data =  localDb.postDao().getAllPosts();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->callback.onComplete(data));
//        });
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
