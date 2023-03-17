package com.example.getmenu.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.getmenu.MyApplication;
import com.example.getmenu.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FireBaseModel {

    FirebaseFirestore db;

    public FireBaseModel(){
        db = FirebaseFirestore.getInstance();
        // Disable Firestore caching
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
//        db.setFirestoreSettings(settings);
    }

    public interface Listener<T> {
        void onComplete();

        void onFail();
    }

    public static void getAllPosts(Long lastUpdateDate, Model.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Post.POST_COLLECTION_NAME)
                .whereGreaterThan("updateDate", new Timestamp(lastUpdateDate, 0)).get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<Post>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public static void addPost(Post post , Uri imageUri, Model.AddPostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Post.POST_COLLECTION_NAME)
                .add(post.toJson("create")).addOnSuccessListener(documentReference -> {
                    Utils.print("Added new post with id : " + documentReference.getId());

                    //image upload
                    uploadImage(documentReference.getId(), imageUri, post.getImageVersion(), Post.POST_IMAGES_FIREBASE_PATH)
                            .addOnCompleteListener(task -> {

                                documentReference.update(Post.POST_IMAGE_FIELD_NAME, task.getResult().toString());
                                documentReference.update("id", documentReference.getId());

                                post.setPostImageUrl(task.getResult().toString());
                                post.setId(documentReference.getId());

                                listener.onComplete();
                            });
                });
    }


    public static void editPost(Post post, Uri imageUri, boolean uploadImage, FireBaseModel.Listener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (uploadImage) {
            uploadImage(post.getId(), imageUri, post.getImageVersion(), Post.POST_IMAGES_FIREBASE_PATH)
                    .addOnCompleteListener(task -> {
                        post.setPostImageUrl(task.getResult().toString());
                        db.collection(Post.POST_COLLECTION_NAME).document(post.getId()).update(post.toJson("edit"))
                                .addOnSuccessListener(documentReference -> {
                                    listener.onComplete();
                                });
                    });
        } else {
            db.collection(Post.POST_COLLECTION_NAME).document(post.getId()).update(post.toJson("edit")).addOnSuccessListener(
                    documentReference -> {
                        listener.onComplete();
                    });
        }
    }

    public static void getAllPostsByUserId(String userId, Model.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Post.POST_COLLECTION_NAME)
                .whereEqualTo("userId", userId).get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<Post>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public static void deletePost(Post post, Uri imageUri, FireBaseModel.Listener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> json = new HashMap<>();
        json.put("isDeleted", true);
        json.put("updateDate", FieldValue.serverTimestamp());

        db.collection(Post.POST_COLLECTION_NAME).document(post.getId()).update(json).addOnSuccessListener(
                documentReference -> {

                    Utils.print("Deleted post with id : " + post.getId());
                    listener.onComplete();
                }
        );

    }


    public static Task<Uri> uploadImage(String id, Uri imageUri, int imageVersion, String firebasePath) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(firebasePath);
        String imageName = id + "-" + imageVersion + "." + getExtension(imageUri);

        final StorageReference imageRef = storageReference.child(imageName);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return imageRef.getDownloadUrl();
        });
    }

    public static String getExtension(Uri uri) {
        try {
            ContentResolver contentResolver = MyApplication.context.getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        } catch (Exception e) {
            Toast.makeText(MyApplication.context, "Register page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public void addUser(User user, Uri imageUri, Model.AddUserListener addUserListener) {
        uploadImage(user.getId(), imageUri, user.getImageVersion(), User.USERS_IMAGES_FIREBASE_PATH)
                .addOnCompleteListener(task -> {
                    user.setProfileImageUrl(task.getResult().toString());
                    db.collection(User.COLLECTION_NAME).document(user.getId())
                            .set(user.toJson())
                            .addOnSuccessListener(documentReference -> {
                                addUserListener.onComplete();
                            })
                            .addOnFailureListener(e -> {
                            });
                });
    }

    public void editUser(User user, Uri imageUri, boolean uploadImage, Model.AddUserListener addUserListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (uploadImage) {
            int imgVersion = user.getImageVersion() + 1;
            user.setImageVersion(imgVersion);

            uploadImage(user.getId(), imageUri, imgVersion, User.USERS_IMAGES_FIREBASE_PATH)
                    .addOnCompleteListener(task -> {
                        user.setProfileImageUrl(task.getResult().toString());
                        db.collection(User.COLLECTION_NAME).document(user.getId()).update(user.toJson())
                                .addOnSuccessListener(documentReference -> {
                                    addUserListener.onComplete();
                                });
                    });
        } else {
            db.collection(User.COLLECTION_NAME).document(user.getId()).update(user.toJson()).addOnSuccessListener(
                    documentReference -> {
                        addUserListener.onComplete();
                    });
        }
    }

    public static void loginUser(final String email, String password, final FireBaseModel.Listener listener) {
        Log.d("TAG", email + password);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (email != null && !email.equals("") && password != null && !password.equals("")) {
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.signOut();
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).
                    addOnSuccessListener(authResult -> {
                        Utils.print("Authentication successes");
                        listener.onComplete();
                    }).addOnFailureListener(e -> {
                        Log.d("TAG", "failed");
                        listener.onFail();
                    });
        } else {
        }
    }

    public static void register(String email, String password, FireBaseModel.Listener<Boolean> listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    MyApplication.getUser().setId(authResult.getUser().getUid());
                    listener.onComplete();
                })
                .addOnFailureListener(e -> listener.onFail());
    }

    public static void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        MyApplication.signOut();
    }

    public static void getUserById(String id,Model.GetUserListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(User.COLLECTION_NAME)
                .whereEqualTo("id", id).get()
                .addOnCompleteListener(task -> {
                    User user = new User();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            user = User.create(doc.getData());
                        }
                    }
                    listener.onComplete(user);
                });




//         db.collection(User.COLLECTION_NAME).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                User user = new User();
//                if (task.isSuccessful()) {
//                    user = User.create(task.getResult().getData());
//                }
//                listener.onComplete(user);
//            }
//        });
    }


}
