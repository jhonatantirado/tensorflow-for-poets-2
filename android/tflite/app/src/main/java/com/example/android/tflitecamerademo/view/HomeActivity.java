package com.example.android.tflitecamerademo.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tflitecamerademo.ImageClassifier;
import com.example.android.tflitecamerademo.R;
import com.example.android.tflitecamerademo.util.TakePictureUtil;

import java.io.IOException;

public class HomeActivity extends Activity {

    private Button btnCamera;
    private Button btnGallery;
    private ImageView ivPhoto;
    private TextView tvData;
    private Context context;
    private String cameraPermission = Manifest.permission.CAMERA;
    private String readStorePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String writeStorePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int REQUEST_TAKE_PHOTO = 122;
    private final int REQUEST_CAMERA = 123;
    private final int REQUEST_READ_STORAGE = 124;
    private final int REQUEST_PERMISSIONS = 130;

    private String authority = "com.example.android.tflitecamerademo.provider";
    private Uri uri;
    private ImageClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        ivPhoto = findViewById(R.id.ivPhoto);
        tvData = findViewById(R.id.tvData);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(context, cameraPermission)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(cameraPermission);
                    return;
                }
                //uri = TakePictureUtil.takePictureToGallery(context, REQUEST_TAKE_PHOTO);
                uri = TakePictureUtil.createIntentPicture(context, REQUEST_TAKE_PHOTO);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission(new String[]{cameraPermission, readStorePermission, writeStorePermission});
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{permission},
                REQUEST_CAMERA);
    }

    private void requestPermission(String[] permission) {
        ActivityCompat.requestPermissions(HomeActivity.this,
                permission,
                REQUEST_PERMISSIONS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //uri = TakePictureUtil.takePictureToGallery(context, REQUEST_TAKE_PHOTO);
                    uri = TakePictureUtil.createIntentPicture(context, REQUEST_TAKE_PHOTO);
                }
            }
            case REQUEST_PERMISSIONS: {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap bm = null;

                if (data == null)
                    bm = TakePictureUtil.getBitmapPictureFromMedia(context, uri);
                else
                    bm = TakePictureUtil.getBitmapPictureFromMedia(context, data.getData());

                if (bm == null)
                    return;

                Bitmap bnResized = TakePictureUtil.getResizedBitmap(bm, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);
                ivPhoto.setImageBitmap(bnResized);

                classifier = new ImageClassifier(this);
                String textToShow = classifier.classifyFrame(bnResized);
                tvData.setText(textToShow);
            } catch (IOException e) {
                //"Failed to initialize an image classifier.
            }
        }
    }
}
