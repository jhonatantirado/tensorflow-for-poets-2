package com.example.android.tflitecamerademo.model;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Jeancarlos Paucar on 9/20/2017.
 */

public class BECamera {

    public Intent intent;
    public String path;
    public File file;
    public boolean validPhoto;
    public Uri uri;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isValidPhoto() {
        return validPhoto;
    }

    public void setValidPhoto(boolean validPhoto) {
        this.validPhoto = validPhoto;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}