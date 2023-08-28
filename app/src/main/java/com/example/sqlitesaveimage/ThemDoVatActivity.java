package com.example.sqlitesaveimage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class ThemDoVatActivity extends AppCompatActivity {

    Button btnAdd, btnHuy;
    EditText edtTen, edtMota;
    ImageButton ibtnCamera, ibtnFolder;

    ImageView imgHinh;



    // for startActivityForResult()
    // int REQUEST_CODE_CAMERA = 123;

//    private ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
//                }
//            });

    // gộp Folder và Camera
    private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // xử lý kết quả
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imgHinh.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imgHinh.setImageBitmap(bitmap);
                    }


                }
            });

    // Cách 1 thay thế cho startActivityForResult được khuyến khích
    // imgCamera
//    private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    // xử lý kết quả
//                    Intent data = result.getData();
//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                    imgHinh.setImageBitmap(bitmap);
//                }
//            });

    // imgFolder
//    private ActivityResultLauncher<Intent> myIntentActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                if  (result.getResultCode() == RESULT_OK) {
//                    Intent data = result.getData();
//                    Uri uri = data.getData();
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(uri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        imgHinh.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_do_vat);

        AnhXa();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chuyển data imgView -> byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] hinhAnh = byteArrayOutputStream.toByteArray();

                // resize kích thước
                while (hinhAnh.length > 500000) {
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap1, (int)(bitmap1.getWidth()*0.8), (int)(bitmap1.getHeight()*0.8), true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    hinhAnh = stream.toByteArray();
                }

                MainActivity.database.INSERT_DOVAT(
                        edtTen.getText().toString().trim(),
                        edtMota.getText().toString().trim(),
                        hinhAnh
                        );
                Toast.makeText(ThemDoVatActivity.this, "Đã Thêm!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ThemDoVatActivity.this, MainActivity.class));
            }
        });

        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
//                if (ContextCompat.checkSelfPermission(
//                        ThemDoVatActivity.this, Manifest.permission.CAMERA) ==
//                        PackageManager.PERMISSION_GRANTED) {
//                    // You can use the API that requires the permission.
//
//
//
//                } else {
//                    // You can directly ask for the permission.
//                    // The registered ActivityResultCallback gets the result of this request.
//                    requestPermissionLauncher.launch(
//                            Manifest.permission.CAMERA);
//                }
                // cách 1
                 intentActivityResultLauncher.launch(intent);
            //    Cách 2 thay thế cho startActivityForResult() nhưng không khuyến khích
            //    ActivityCompat.startActivityForResult(ThemDoVatActivity.this, intent, REQUEST_CODE_CAMERA, null);
            }
        });

        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intentActivityResultLauncher.launch(intent);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void AnhXa() {
        btnAdd      = findViewById(R.id.buttonAdd);
        btnHuy      = findViewById(R.id.buttonHuy);
        edtTen      = findViewById(R.id.editTextTenDoVat);
        edtMota     = findViewById(R.id.editTextMoTa);
        ibtnCamera  = findViewById(R.id.imageCamera);
        ibtnFolder  = findViewById(R.id.imageFolder);
        imgHinh     = findViewById(R.id.imageHinh);
    }


}