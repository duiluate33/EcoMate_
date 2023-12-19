package kr.ac.yeonsung.seoj.ecomate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout.DrawerListener ActionBarDrawerToggle;
    ImageView imageView;
    File file;
    Bitmap bitmap;
    Button btncamera, btngallery, appguide, trashguide;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;



    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0 ;
    static final int REQUEST_TAKE_GALLERY = 10;

    public static Context mContext;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* mModule = LiteModuleLoader.load(MainActivity.assetFilePath(getApplicationContext(), "best.torchscript.ptl"));
        BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("aicook.txt")));*/

        //File sdcard = Environment.getExternalStorageDirectory();
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());


        imageView = findViewById(R.id.imageView);
        btncamera = findViewById(R.id.btn_camera);
        btngallery = findViewById(R.id.btn_gallery);
        appguide = findViewById(R.id.app_guide);
        trashguide = findViewById(R.id.trash_guide);





        //권한 체크
        boolean hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시  권한설정 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_TAKE_GALLERY);
            }
        });

        Uri photoUri;

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO);
            }
        });

        mContext = this;
    }


    //메뉴바 가이드 화면설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("test","menuButton create");
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.drawerlayout,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 여기 자체가 안들어오는구만
        //코드 잘못짠건가여,,분명 맞게짯는데 구글링햇는데 똑같앗아여
        //super.onOptionsItemSelected(item);
            Log.d("test","menuButton START");
        switch (item.getItemId()) {

            case R.id.app_guide:
                Log.d("test","첫번째");
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
                

                break;

            case R.id.trash_guide:
                Log.d("test","두번째");
                intent = new Intent(MainActivity.this, GuideActivity_Trash.class);
                startActivity(intent);

                break;
        }
        return true;
    }





    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA},11);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11){
            if(grantResults.length>0){
                if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                // Bundle로 데이터를 입력
                Bundle extras = data.getExtras();
                // Bitmap으로 컨버전
                bitmap = (Bitmap) extras.get("data");

                if(bitmap != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    float scale = (float) (1024/(float)bitmap.getWidth());
                    int image_w = (int) (bitmap.getWidth() * scale);
                    int image_h = (int) (bitmap.getHeight() * scale);
                    Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                    resize.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("image", byteArray);

                    setResult(resultCode);

                    startActivity(intent);
                }
            }
        }
        else if(requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK){
            if (data != null) {

                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지뷰에 세팅

                    if(bitmap != null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        float scale = (float) (1024/(float)bitmap.getWidth());
                        int image_w = (int) (bitmap.getWidth() * scale);
                        int image_h = (int) (bitmap.getHeight() * scale);
                        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                        resize.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] byteArray = stream.toByteArray();

                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtra("image", byteArray);

                        setResult(resultCode);

                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    }

    //카메라로 촬영한 이미지를 파일로 저장해주는 함수 생성
    private File createImageFile() throws IOException{
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                StorageDir
        );
        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //카메라 인텐트를 실행하는 부분을 별도 함수로 생성
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //ensure that's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //찍은 이미지 파일을 어디로 보낼지 생성
            File photofile = null;
            try{
                File photoFile = createImageFile();
            }
            catch (IOException ex) {
                // create file했을 때 발생하는 에러
            }

            //파일이 성공적으로 생성됐을 때 continue
            if (photofile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "kr.ac.yeonsung.seoj.fileprovider", photofile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }

        }
    }

}