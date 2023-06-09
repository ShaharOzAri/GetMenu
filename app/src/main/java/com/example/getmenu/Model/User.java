package com.example.getmenu.Model;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity
public class User implements Serializable {
    final public static String COLLECTION_NAME = "users";
    public static String USERS_IMAGES_FIREBASE_PATH = "images/users";

    int imageVersion = 0;

    @PrimaryKey
    @NotNull
    String id = "";

    @NotNull
    String name = "";

    @NotNull
    String email = "";

    @NotNull
    String profileImageUrl = "";

    //function that create map(json) from user
    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", id);
        json.put("email", email);
        json.put("name", name);
        json.put("imageVersion", imageVersion);
        json.put("profileImageUrl", profileImageUrl);

        return json;
    }

    //function that create user from map(json)
    public static User create(Map<String, Object> json) {
        User user = new User();

        if (json.containsKey("id")) {
            user.setId((String) json.get("id"));
        }

        if (json.containsKey("email")) {
            user.setEmail((String) json.get("email"));
        }

        if (json.containsKey("name")) {
            user.setName((String) json.get("name"));
        }

        if (json.containsKey("imageVersion")) {
            user.setImageVersion(Integer.parseInt(json.get("imageVersion").toString()));
        }

        if (json.containsKey("profileImageUrl")) {
            user.setProfileImageUrl((String) json.get("profileImageUrl"));
        }
        return user;
    }
        // Getters ------------------------------------------------------------------------
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getImageVersion() {
        return imageVersion;
    }

    // Setters ------------------------------------------------------------------------
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageVersion(int imageVersion) {
        this.imageVersion = imageVersion;
    }
}
