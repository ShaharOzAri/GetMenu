package com.example.getmenu.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FireBaseModel {

    FirebaseFirestore db;

    FireBaseModel(){
        db = FirebaseFirestore.getInstance();
        // Disable Firestore caching
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getAllPosts(Model.GetAllPostsListener callback){
        db.collection(Post.POST_COLLECTION_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts = new LinkedList<>();
                if(task.isSuccessful()){
                    QuerySnapshot jsonsList = task.getResult();
                    for(DocumentSnapshot json:jsonsList){
                        posts.add(Post.create(json.getData()));
                    }
                }
                callback.onComplete(posts);
            }
        });
    }
    public void addPost(Post post , Model.AddPostListener listener) {
        db.collection(Post.POST_COLLECTION_NAME)
                .add(post.toJson())
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        listener.onComplete();
                    }
                });
    }



}
