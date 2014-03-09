package com.example.cameffect;

import java.io.ByteArrayOutputStream;

//import com.example.camtest.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity implements Camera.PreviewCallback {
	int numberOfCameras;
	int defaultCameraId;
	ImageView imgVieW;
	Camera mCamera;
	MenuItem SaveMnuItem;
    int cameraCurrentlyLocked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		numberOfCameras = Camera.getNumberOfCameras();
		imgVieW= (ImageView)findViewById(R.id.imageView1);
		mCamera = Camera.open();
		CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
        	Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
            	defaultCameraId = i;
            }
        }
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.exit:
			finish();
			System.exit(0);
		case R.id.take:
			if("Take Picture".equals(item.getTitle())){
				item.setTitle("Start Camera");
				mCamera.stopPreview();
				SaveMnuItem.setVisible(true);
				}
			else{
				item.setTitle("Take Picture");
				mCamera.startPreview();
				SaveMnuItem.setVisible(false);
			}
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SaveMnuItem = menu.findItem(R.id.save);
		SaveMnuItem.setVisible(false);
		return true;
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		if (data != null) {
	        //Log.i("DEBUG", "data Not Null");

            // Preprocessing
	        //Log.i("DEBUG", "Try For Image Processing");
            Camera.Parameters mParameters = camera.getParameters();
            Size mSize = mParameters.getPreviewSize();

            int mWidth = mSize.width;
            int mHeight = mSize.height;
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
            yuvImage.compressToJpeg(new Rect(0, 0, mWidth, mHeight), 50, out);
            
            byte[] imageBytes = out.toByteArray();
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            
            imgVieW.setImageBitmap(image);
            camera.addCallbackBuffer(data);
		}
	}

}
