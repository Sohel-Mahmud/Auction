package com.devlearn.sohel.auction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devlearn.sohel.auction.Model.ModelAddAuction;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAuctionActivity extends AppCompatActivity {

    private EditText edtAuctionTitle, edtAuctionDescription, edtAuctionPrice;
    private TextView txtDate, txtTime;
    private ImageView imgAuctionImage;

    private Button btnDate, btnTime, btnAddPhoto, btnPostAcution;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private UserSharedPref usp;

    private int user_id;

    private String val_date="", val_time="";
    Dialog dialog01;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int IMG_REQUEST = 1;
    Bitmap bitmap;
    ProgressDialog progressDoalog;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private String auctionTitle, auctionDetails, auctionValidity="";
    private double auctionPrice=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auction);

        usp = new UserSharedPref(AddAuctionActivity.this);
        user_id = usp.getUser_id();

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        edtAuctionTitle = findViewById(R.id.auction_title);
        edtAuctionDescription = findViewById(R.id.auction_description);
        edtAuctionPrice = findViewById(R.id.auction_price);

        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);

        imgAuctionImage = findViewById(R.id.auction_img);

        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        btnAddPhoto = findViewById(R.id.add_auction_photo);
        btnPostAcution = findViewById(R.id.post_auction);

        dialog01 = new Dialog(AddAuctionActivity.this);
        dialog01.setContentView(R.layout.time_picker);
        final Button ok = dialog01.findViewById(R.id.ok);
        final TimePicker picker=(TimePicker)dialog01.findViewById(R.id.timePicker1);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddAuctionActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow();
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                val_date = year  + "-" +  month + "-" + day;
                txtDate.setText(val_date);
            }
        };

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog01.show();
                int hourr, minutee;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hourr = picker.getHour();
                    minutee = picker.getMinute();
                }
                else{
                    hourr = picker.getCurrentHour();
                    minutee = picker.getCurrentMinute();
                }
                String hour = String.valueOf(hourr);
                String minute = String.valueOf(minutee);
                int n = hour.length();
                int p = minute.length();
                if(n==1){
                    hour= "0"+hour;
                }
                if(p==1){
                    minute= "0"+minute;
                }

                val_time = hour +":"+ minute+":00";
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtTime.setText(val_time);
                        dialog01.cancel();
                    }
                });

            }

        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        btnPostAcution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auctionTitle = edtAuctionTitle.getText().toString().trim();
                auctionDetails = edtAuctionDescription.getText().toString();
                String price = edtAuctionPrice.getText().toString();
                if(!price.isEmpty()){
                    auctionPrice = Double.parseDouble(edtAuctionPrice.getText().toString());
                }
                auctionValidity = val_date+" "+val_time;

                if(TextUtils.isEmpty(auctionTitle) || TextUtils.isEmpty(auctionDetails) || auctionPrice==0.0
                        || TextUtils.isEmpty(auctionValidity) || TextUtils.isEmpty(val_date) || TextUtils.isEmpty(val_time)){
                    Toast.makeText(AddAuctionActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }else if(bitmap == null)
                {
                    Toast.makeText(AddAuctionActivity.this, "Please add a photo", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadPost(auctionTitle,auctionDetails,auctionPrice,auctionValidity,user_id);
                }
            }
        });

    }

    private void uploadPost(String auctionTitle, String auctionDetails, double auctionPrice, String auctionValidity, int user_id) {
        String image = imageToString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressDoalog = new ProgressDialog(AddAuctionActivity.this);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.show();

        Call<ModelAddAuction> call = apiInterface.upload_auction(user_id, auctionTitle, auctionDetails, auctionPrice, auctionValidity, image);
        call.enqueue(new Callback<ModelAddAuction>() {
            @Override
            public void onResponse(Call<ModelAddAuction> call, Response<ModelAddAuction> response) {
                if(response.body().equals(null))
                {
                    Toast.makeText(AddAuctionActivity.this, "Something went wrong with response", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
                else{
                    Toast.makeText(AddAuctionActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    imgAuctionImage.setVisibility(View.GONE);
                    startActivity(new Intent(AddAuctionActivity.this, MainBuySellActivity.class));
                    finish();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ModelAddAuction> call, Throwable t) {
                Toast.makeText(AddAuctionActivity.this, "Check Internet connection "+t.getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });
    }

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(AddAuctionActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AddAuctionActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AddAuctionActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AddAuctionActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddAuctionActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddAuctionActivity.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAuctionActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddAuctionActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAuctionActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", AddAuctionActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(AddAuctionActivity.this, "Go to Permissions to Grant  Camera", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(AddAuctionActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            Toast.makeText(AddAuctionActivity.this, "Permissions Required", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.putBoolean(permissionsRequired[1],true);
            editor.putBoolean(permissionsRequired[2],true);
            editor.apply();
        } else {

            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgAuctionImage.setImageBitmap(bitmap);
                imgAuctionImage.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private String imageToString(){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imagebyte,Base64.DEFAULT);

    }
}
