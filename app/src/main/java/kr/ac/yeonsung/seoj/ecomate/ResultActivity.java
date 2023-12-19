package kr.ac.yeonsung.seoj.ecomate;

import static java.sql.Types.NULL;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ResultActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar); //툴바를 액션바로 설정
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 활성화

        ImageView imageview = (ImageView)findViewById(R.id.Image_Viewer);

        Button btn_result_main = findViewById(R.id.btn_result_gomain);
        Button btn_result_selectimage = findViewById(R.id.btn_result_selectimage);
        Button btn_result_camera = findViewById(R.id.btn_result_camera);

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        // 이미지가 있는 경우에만
        if (bitmap != null) {
            /*int nTargetW = imageview.getWidth();
            int nTargetH = imageview.getHeight();

            int PhotoW = bitmap.getWidth();
            int PhotoH = bitmap.getHeight();
            float rate = 0.0f;

            if(nTargetH < PhotoH || nTargetW < PhotoW) {
                Bitmap.createScaledBitmap(bitmap, nTargetW, nTargetH, false);
            }*/
            imageview.setImageBitmap(bitmap);
        }

        btn_result_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btn_result_selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_TAKE_GALLERY);
            }
        });

        btn_result_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try{
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                }
                case REQUEST_TAKE_GALLERY: {
                    if (resultCode == RESULT_OK) {
                        try {
                            // 선택한 이미지에서 비트맵 생성
                            InputStream in = getContentResolver().openInputStream(intent.getData());
                            bitmap = BitmapFactory.decodeStream(in);
                            in.close();
                            // 이미지뷰에 세팅
                            if(bitmap != null){
                                imageView.setImageBitmap(bitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_result_camera:
                ((MainActivity)MainActivity.mContext).dispatchTakePictureIntent();
                break;
        }
    }


}