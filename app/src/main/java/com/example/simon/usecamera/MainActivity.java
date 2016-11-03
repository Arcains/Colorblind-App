package com.example.simon.usecamera;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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
    String imgDecodeableString;
    static final int RESULT_LOAD_IMG = 1;
    static final int CAM_REQUEST = 2;
    static int counter = 1;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    public void galleryButtonClicked(View view) {
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

        if (imageView.getDrawable() != null) {
            bitmap = FilterUtils.imageToBitmap(imageView);
            bitmap = FilterUtils.modifyPixels(bitmap);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "select a picture first", Toast.LENGTH_LONG)
                    .show();
        }

    }


    private File getFile() {
        File folder = new File("sdcard/camera_app");

        counter++;

        if (!folder.exists()) {
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

        } else {

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
                    imgDecodeableString = cursor.getString(columnIndex);
                    cursor.close();
                    ImageView imgView = (ImageView) findViewById(R.id.image_view);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodeableString));

                } else {
                    Toast.makeText(this, "You haven't picked an Image",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }
}
