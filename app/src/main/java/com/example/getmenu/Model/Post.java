package com.example.getmenu.Model;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Calendar;
import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import com.google.firebase.firestore.FieldValue;


@Entity
public class Post implements Serializable {
    public static String POST_LAST_UPDATE = "PostLastUpdateDate";
    public static String POST_COLLECTION_NAME = "posts";
    public static String POST_IMAGES_FIREBASE_PATH = "images/posts/";
    public static String POST_IMAGE_FIELD_NAME = "postImageUrl";

    @PrimaryKey
    @NotNull
    String id = "";
    String title = "";
    String userId = "";
    String userName = "";
    String postImageUrl = "";
    String userProfileUrl = "";


    int imageVersion = 0;

    boolean isDeleted = false;

    Long timestamp = new Long(0);
    Long updateDate = new Long(0);

    public Post(String i , String title) {
        id = i;
        this.title = title;
        userName = "shahar";
        postImageUrl = "https://cdn.onecklace.com/products/2653/product_2653_1_730.jpeg";
        userProfileUrl = "https://cdn.onecklace.com/products/2653/product_2653_model_1_730.jpeg";
    }

    public Post(){}
    public Map<String, Object> toJson(String action) {
        Map<String, Object> json = new HashMap<String, Object>();

        json.put("id", id);
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("userProfileUrl", userProfileUrl);
        json.put("postImageUrl", postImageUrl);
        json.put("title", title);
        json.put("isDeleted", isDeleted);
        json.put("imageVersion", imageVersion);
        json.put("updateDate", FieldValue.serverTimestamp());
//
//        if ("create".equals(action)) {
//            json.put("timestamp", FieldValue.serverTimestamp());
//        } else {
//            Timestamp ts = getServerTimestamp(timestamp);
//            json.put("timestamp", ts);
//        }
//
        return json;
    }

    public static Post create(Map<String, Object> json) {
        Post post = new Post();

        if (json.containsKey("id")) {
            post.setId((String) json.get("id"));
        }

        if (json.containsKey("userName")) {
            post.setUserName((String) json.get("userName"));
        }

        if (json.containsKey("userId")) {
            post.setUserId((String) json.get("userId"));
        }

        if (json.containsKey("title")) {
            post.setTitle((String) json.get("title"));
        }

        if (json.containsKey("postImageUrl")) {
            post.setPostImageUrl((String) json.get("postImageUrl"));
        }


        if (json.containsKey("userProfileUrl")) {
            post.setUserProfileUrl((String) json.get("userProfileUrl"));
        }

        if (json.containsKey("isDeleted")) {
            post.setDeleted(Boolean.parseBoolean(json.get("isDeleted").toString()));
        }

        if (json.containsKey("imageVersion")) {
            post.setImageVersion(Integer.parseInt(json.get("imageVersion").toString()));
        }


//        if (json.containsKey("timestamp")) {
//            try {
//                Timestamp ts = (Timestamp) json.get("timestamp");
//                Long timestamp = ts.getSeconds();
//                post.setTimestamp(timestamp);
//            } catch (Exception e) {
//                post.setTimestamp(Long.parseLong(json.get("timestamp").toString()));
//                e.printStackTrace();
//            }
//        }

//        if (json.containsKey("updateDate")) {
//            Timestamp ts = (Timestamp) json.get("updateDate");
//            Long updateDate = ts.getSeconds();
//            post.setUpdateDate(updateDate);
//        }

        return post;
    }

    // Getters ------------------------------------------------------------------------
    @NotNull
    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public int getImageVersion() {
        return imageVersion;
    }


    // Setters ------------------------------------------------------------------------
    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    // Common ------------------------------------------------------------------------
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setImageVersion(int imageVersion) {
        this.imageVersion = imageVersion;
    }

//    private Timestamp getServerTimestamp(long timestamp) {
//        TimeZone tz = TimeZone.getDefault();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
//
//        return new Timestamp(new Date(timestamp * 1000));
//    }
}
