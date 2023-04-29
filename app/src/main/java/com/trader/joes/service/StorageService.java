package com.trader.joes.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class StorageService {

    private StorageReference profilePicDirRef;

    public StorageService() {
         if(profilePicDirRef == null) {
             FirebaseStorage storage = FirebaseStorage.getInstance();
             StorageReference storageRef = storage.getReference();
             profilePicDirRef = storageRef.child("profile-pictures");
         }
    }

    private StorageReference getProfilePicRef() {
        String userId = new AuthService().getCurrentUser().getUid();
        String imgName = userId + ".jpg";
        return this.profilePicDirRef.child(imgName);
    }
    public void saveProfilePic(Bitmap bitmap, Activity activity) {
        StorageReference profilePicRef = getProfilePicRef();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity, "Profile pic uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void downloadProfilePic(ImageView imageView) {
        StorageReference profilePicRef = getProfilePicRef();

        final long ONE_MEGABYTE = 1024 * 1024;
        profilePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false));
            }
        });
    }
}
