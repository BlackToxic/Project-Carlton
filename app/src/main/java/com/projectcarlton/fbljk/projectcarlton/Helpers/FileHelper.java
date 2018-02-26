package com.projectcarlton.fbljk.projectcarlton.Helpers;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class FileHelper {

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}