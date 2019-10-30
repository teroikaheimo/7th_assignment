package com.example.a7th_assignment;

import android.graphics.Bitmap;

public class CustomListItem {
    String owner, license;
    Bitmap image;

    CustomListItem(String _owner, String _licence, Bitmap _image) {
        this.owner = _owner;
        this.license = _licence;
        this.image = _image;
    }
}
