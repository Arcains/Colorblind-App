package com.example.simon.usecamera;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    Button button;
    Button button2;
    Button button3;
    ImageView imageView;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    static final int CAM_REQUEST = 2;
    static int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    public void galleryClicked(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void captureButtonClicked(View v) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent, CAM_REQUEST);

    }


    public void applyColorFilter(View v) {

        BitmapDrawable  abm = (BitmapDrawable)imageView.getDrawable();
        Bitmap bmp = abm.getBitmap();


        // Create a bitmap of the same size
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        // Create a canvas  for new bitmap
        Canvas c = new Canvas(newBmp);
        // Draw old bitmap on it
        c.drawBitmap(bmp, 0, 0, new Paint());

        for(int i=0; i<newBmp.getWidth(); i++){
            for(int j=0; j<newBmp.getHeight(); j++){
                int color = newBmp.getPixel(i, j);


                int red = (color << 8 >>> 24);
                int green = (color << 16 >>> 24);
                int blue = (color << 24 >>> 24);

                if (red <= green) {
                    red = red*9/8;
                    green = green*9/8;
                    blue = blue*9/8;

                } else {
                    red = red*8/9;
                    green = green*8/9;
                    blue = blue*8/9;
                }

                if (red > 255) red = 255;
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;

                color = red << 16 | green << 8 | blue;

                newBmp.setPixel(i, j, color);

            }
        }

        imageView.setImageBitmap(newBmp);

    }



    private File getFile() {
        File folder = new File("sdcard/camera_app");

        counter++;

        if(!folder.exists()) {
            folder.mkdir();
        }

        File image_file = new File(folder, "cam_image".concat(Integer.toString(counter)).concat(".jpg"));
        return image_file;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST) {

            String path = "sdcard/camera_app/cam_image".concat(Integer.toString(counter)).concat(".jpg");
            imageView.setImageDrawable(Drawable.createFromPath(path));

        }

        else {

            try {
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    ImageView imgView = (ImageView) findViewById(R.id.image_view);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));

                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }



}
