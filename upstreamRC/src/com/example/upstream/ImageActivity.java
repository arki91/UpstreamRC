package com.example.upstream;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageActivity {
	MainActivity main;
	ImageView imView;
	
	
	public ImageActivity(MainActivity main, ImageView image) {
		this.main=main;
		imView=image;
	}

	public void zeichne(Bitmap bild){
		imView.setImageBitmap(bild);
	}
	
}
