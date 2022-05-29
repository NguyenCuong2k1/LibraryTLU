package com.nguyencuong.librarytlu.act;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nguyencuong.librarytlu.R;
import com.nguyencuong.librarytlu.model.LoginDataUserRespone;
import com.nguyencuong.librarytlu.retrofit.APIService;
import com.nguyencuong.librarytlu.retrofit.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
    EditText edtPass, edtName;
    Button btnLoginUser, btnLoginLibrarian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();
        initView();
        addEvent();
    }

    private void addEvent() {
        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginUser.setClickable(false);

                Handler handler1 = new Handler();

                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnLoginUser.setClickable(true);
                    }
                }, 1500);

                String username = edtName.getText().toString();
                String pass = edtPass.getText().toString();

                if(username == null || username.equals("")) {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập không được trống", Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass == null || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_LONG).show();
                    return;
                } else if (pass.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu phải dài hơn 6 kí tự!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(LoginActivity.this, "Thông tin hợp lệ!", Toast.LENGTH_LONG).show();
                }

                handleLogin(username, pass);
            }
        });

//        btnLoginLibrarian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnLoginLibrarian.setClickable(false);
//
//                Handler handler1 = new Handler();
//
//                handler1.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        btnLoginLibrarian.setClickable(true);
//                    }
//                }, 1500);
//
//                String username = edtName.getText().toString();
//                String pass = edtPass.getText().toString();
//
//                if(username == null || username.equals("")) {
//                    Toast.makeText(LoginActivity.this, "Tên đăng nhập không được trống", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if(pass == null || pass.equals("")) {
//                    Toast.makeText(LoginActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_LONG).show();
//                    return;
//                } else if (pass.length() < 6) {
//                    Toast.makeText(LoginActivity.this, "Mật khẩu phải dài hơn 6 kí tự!", Toast.LENGTH_LONG).show();
//                    return;
//                } else {
//                    Toast.makeText(LoginActivity.this, "Thông tin hợp lệ!", Toast.LENGTH_LONG).show();
//                }
//
//                handleLoginLibrarian(username, pass);
//
//
//            }
//        });
    }

    private void handleLoginLibrarian(String username, String pass) {
        DataService dataService = APIService.getService();

    }

    private void handleLogin(String username, String pass) {
        DataService dataService = APIService.getService();
        Call<List<LoginDataUserRespone>> call = dataService.getDataLoginStudent(username, pass);
        Log.d(TAG, call.request().toString());

        call.enqueue(new Callback<List<LoginDataUserRespone>>() {
            @Override
            public void onResponse(Call<List<LoginDataUserRespone>> call, Response<List<LoginDataUserRespone>> response) {
                List<LoginDataUserRespone> loginDataUserRespones = response.body();
                Log.d(TAG, loginDataUserRespones.toString());

                if(loginDataUserRespones != null && loginDataUserRespones.size() > 0) {
                    LoginDataUserRespone loginDataUserRespone = loginDataUserRespones.get(0);
                    Log.d(TAG, loginDataUserRespone.toString());

                    Gson gson = new Gson();
                    String json = gson.toJson(loginDataUserRespone);
                    Log.d(TAG, "Json: "+ json);

                    saveAccount(json);

                    Intent intent = new Intent(LoginActivity.this, MainActivityStudent.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoginDataUserRespone>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });



    }

    private void saveAccount(String json) {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AccountUser", json);
        editor.apply();
    }

    private void initView() {
        checkAccount();
    }

    private void mapping() {
        edtName = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPass);
        btnLoginUser = findViewById(R.id.btnLoginUser);
    }

    private void checkAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE);
        String json = sharedPreferences.getString("AccountUser", null);
        int type = sharedPreferences.getInt("Type", -1);
        if(json != null && type != -1) {
            Gson gson = new Gson();
            LoginDataUserRespone dataLoginStudent = gson.fromJson(json, LoginDataUserRespone.class);
            Log.d(TAG, dataLoginStudent.toString());

            if(type == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivityStudent.class);
                startActivity(intent);
                finish();
            } else if(type == 1) {
                Intent intent = new Intent(LoginActivity.this, MainActivityLibrarian.class);
                startActivity(intent);
                finish();
            }
        }
    }
}