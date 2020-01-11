package com.example.android.tflitecamerademo.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.example.android.tflitecamerademo.R;
import com.example.android.tflitecamerademo.model.BECamera;

import java.util.ArrayList;
import java.util.List;

public class TakePictureUtil {

    public static Uri createIntentPicture(Context context, int requestId) {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();

        BECamera beCamera = createCameraIntent(context);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = beCamera.getIntent();

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.camera_util_select_picture));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        ((Activity) context).startActivityForResult(chooserIntent, requestId);
        return beCamera.getUri();
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {

        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    public static BECamera createCameraIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ContentValues values = new ContentValues(3);
        String displayName = "FromTakepicture." + System.currentTimeMillis() + ".jpg";
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri photoURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        BECamera beCamera = new BECamera();
        beCamera.setIntent(takePictureIntent);
        beCamera.setUri(photoURI);
        return beCamera;
    }

    public static Bitmap getBitmapPictureFromMedia(Context context, Uri uri) {
        if (uri != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor2 = context.getContentResolver().query(
                    uri, filePathColumn, null, null, null);

            if (cursor2 != null) {

                cursor2.moveToFirst();

                int columnIndex = cursor2.getColumnIndex(filePathColumn[0]);
                String filePath = cursor2.getString(columnIndex);
                cursor2.close();
                return BitmapFactory.decodeFile(filePath);
            }
        }
        return null;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        if (bm == null)
            return null;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}