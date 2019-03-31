package com.devlearn.sohel.auction.LoginRegisterPanel;

import android.animation.LayoutTransition;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devlearn.sohel.auction.AdminPanel.AdminPanelActivity;
import com.devlearn.sohel.auction.MainBuySellActivity;
import com.devlearn.sohel.auction.Model.UserModel;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterActivity extends AppCompatActivity {

    private RelativeLayout login_layout, register_layout;
    private TextView signupTxtButton, loginTxtButton;
    private Button btnLogin, btnRegister;

    private EditText edtEmail, edtPassword;
    private EditText edtRegUsername, edtRegAddress, edtRegPhone, edtRegEmail, edtRegPassword, edtRegConfirmPass;

    private String strEmail, strPassword;
    private String strRegUsername, strRegAddress, strRegPhone, strRegEmail, strRegPassword, strRegConfirmPass;
    private  boolean page = false;
    
    private ProgressDialog progressDialog;

    private int selectedId = -1;
    private String radioStrValue;
    private RadioGroup radioProvider;

    private UserSharedPref usp;
    private String userRole;

    public static final String CHANNEL_ID = "sohel_coding";
    private static final String CHANNEL_NAME = "sohel coding";
    private static final String CHANNEL_DESC = "sohel coding Notification";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        register_layout = findViewById(R.id.register_layout);
        login_layout = findViewById(R.id.login_layout);
        btnLogin = findViewById(R.id.loginbtn);
        btnRegister = findViewById(R.id.register_button);

        signupTxtButton = findViewById(R.id.signup_txt_btn);
        loginTxtButton = findViewById(R.id.login_txt_btn);

        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        
        edtRegUsername = findViewById(R.id.register_username);
        edtRegAddress = findViewById(R.id.register_address);
        edtRegPhone = findViewById(R.id.register_phone);
        edtRegEmail = findViewById(R.id.register_email);
        edtRegConfirmPass = findViewById(R.id.register_confirm_pass);
        edtRegPassword = findViewById(R.id.register_password);

        radioProvider = findViewById(R.id.radioProvider);

        usp = new UserSharedPref(LoginRegisterActivity.this);
        userRole = usp.getUser_role();

        //notification channel for greater than oreo, setting improtance
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //getting device token
        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful())
                        {
                            token = task.getResult().getToken();
                            //saveToken(token);
                        }else{
                            Toast.makeText(LoginRegisterActivity.this, "Eror", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        if(userRole.equals("User"))
        {
            startActivity(new Intent(LoginRegisterActivity.this,MainBuySellActivity.class));
            finish();
        }else if(userRole.equals("Admin"))
        {
            startActivity(new Intent(LoginRegisterActivity.this, AdminPanelActivity.class));
            finish();
        }

        signupTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = true;
                login_layout.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
            }
        });

        loginTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                selectedId = radioProvider.getCheckedRadioButtonId();
                RadioButton provider = radioProvider.findViewById(selectedId);

                radioStrValue = provider.getText().toString();

                if(TextUtils.isEmpty(strEmail))
                {
                    edtEmail.setError("Please insert valid Email");
                    requestFocus(edtEmail);
                }
                else if(TextUtils.isEmpty(strPassword))
                {
                    edtPassword.setError("Insert Password");
                    requestFocus(edtPassword);
                }else if(selectedId == -1)
                {
                    Toast.makeText(LoginRegisterActivity.this, "Please Select Admin Or User", Toast.LENGTH_SHORT).show();
                }
                else if(token.isEmpty())
                {
                    Toast.makeText(LoginRegisterActivity.this, "Failed to get device token", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!radioStrValue.isEmpty())
                    {
                        userLogin(strEmail, strPassword, radioStrValue, token);

                    }else
                    {
                        Toast.makeText(LoginRegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strRegUsername = edtRegUsername.getText().toString().trim();
                strRegAddress = edtRegAddress.getText().toString().trim();
                strRegPhone = edtRegPhone.getText().toString().trim();
                strRegEmail = edtRegEmail.getText().toString().trim();
                strRegConfirmPass = edtRegConfirmPass.getText().toString().trim();
                strRegPassword = edtRegPassword.getText().toString().trim();

                if (TextUtils.isEmpty(strRegUsername) || TextUtils.isEmpty(strRegAddress) || TextUtils.isEmpty(strRegPhone) || TextUtils.isEmpty(strRegEmail) || TextUtils.isEmpty(strRegPassword) || TextUtils.isEmpty(strRegConfirmPass)) {
                    Toast.makeText(LoginRegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (strRegPhone.length() != 11)
                {
                    Toast.makeText(LoginRegisterActivity.this, "Please provide a valid phone Number", Toast.LENGTH_SHORT).show();

                }else if(!strRegPassword.equals(strRegConfirmPass))
                {
                    Toast.makeText(LoginRegisterActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                }else{
                    if(strRegPhone.startsWith("018") || strRegPhone.startsWith("016")
                            || strRegPhone.startsWith("013") || strRegPhone.startsWith("017")
                            || strRegPhone.startsWith("015") || strRegPhone.startsWith("011")
                            || strRegPhone.startsWith("019") || strRegPhone.startsWith("014"))
                    {
                        userRegistration(strRegUsername, strRegAddress, strRegPhone, strRegEmail, strRegPassword);
                    }
                    else{
                        Toast.makeText(LoginRegisterActivity.this, "Please provide a valid phone Number", Toast.LENGTH_SHORT).show();
                    }
                }
                    
                
            }
        });

    }

    private void userLogin(String user_email, String user_password, String user_role, String user_token) {
        progressDialog = new ProgressDialog(LoginRegisterActivity.this);
        progressDialog.setMessage("Login in progress...");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<UserModel> call = apiInterface.user_login(user_email, user_password, user_role, user_token);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserModel> call, Response<UserModel> response) {
                if(response.body()!=null)
                {
                    if(response.body().getResponse().equals("ok"))
                    {
                        usp.setUser_id(response.body().getUser_id());
                        usp.setUser_email(response.body().getUser_email());
                        usp.setUser_name(response.body().getUser_name());
                        usp.setUser_role(response.body().getUser_role());
                        Toast.makeText(LoginRegisterActivity.this, "Logging Success!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if(response.body().getUser_role().equals("User"))
                        {
                            startActivity(new Intent(LoginRegisterActivity.this,MainBuySellActivity.class));
                            finish();
                        }else if(response.body().getUser_role().equals("Admin"))
                        {
                            startActivity(new Intent(LoginRegisterActivity.this,AdminPanelActivity.class));
                            finish();
                        }

                    }else if(response.body().getResponse().equals("failed")){
                        progressDialog.dismiss();
                        Toast.makeText(LoginRegisterActivity.this, "Check your email and password or you are not registered yet", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginRegisterActivity.this, "Something went wrong with response", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<UserModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("DBerrorlogin", "onFailure: "+t.getMessage());
                Toast.makeText(LoginRegisterActivity.this, "Check your Internet "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void userRegistration(String user_name, String user_address, String user_phone, String user_email, String user_password) {
        progressDialog = new ProgressDialog(LoginRegisterActivity.this);
        progressDialog.setMessage("Registration in progress...");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<UserModel> call = apiInterface.set_profile(user_name, user_address, user_phone, user_email,user_password );
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserModel> call, Response<UserModel> response) {
                if(response.body()!=null)
                {
                    if(response.body().getResponse().equals("ok")){
                        Toast.makeText(LoginRegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        edtRegUsername.setText("");
                        edtRegAddress.setText("");
                        edtRegPhone.setText("");
                        edtRegEmail.setText("");
                        edtRegConfirmPass.setText("");
                        edtRegPassword.setText("");
                        login_layout.setVisibility(View.VISIBLE);
                        register_layout.setVisibility(View.GONE);
                    }
                    else if(response.body().getResponse().equals("exist"))
                    {
                        Toast.makeText(LoginRegisterActivity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.body().getResponse().equals("error"))
                    {
                        Toast.makeText(LoginRegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(LoginRegisterActivity.this, "Something went wrong no response", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onFailure(retrofit2.Call<UserModel> call, Throwable t) {
                Log.d("DBerror", t.getMessage());
                Toast.makeText(LoginRegisterActivity.this, "Check your internet connection"+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(page)
        {
            register_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
            page = false;
        }else{
            super.onBackPressed();

        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
