package com.example.getmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public class Utils {

    public static void pickImageFromGallery(Activity sender) {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            Bundle data = null;
            sender.startActivityForResult(openGalleryIntent, 1, data);
            Utils.print(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Print
    public static void print(String str) {
        Log.d("TAG", str);
    }

    //Inputs Validation ------------------------------------

    // Email validation
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // Password validation
    public static boolean isValidPassword(CharSequence target) {
        boolean containsLetters = false;
        boolean containsNumbers = false;
        for (int i = 0; i < target.length(); i++) {

            if (Character.isLetter(target.charAt(i))) {
                containsLetters = true;
            }

            if (Character.isDigit(target.charAt(i))) {
                containsNumbers = true;
            }
        }

        if (target.length() >= 6 && containsLetters && containsNumbers) {
            return true;
        } else {
            return false;
        }
    }
}
