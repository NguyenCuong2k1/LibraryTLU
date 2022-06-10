package com.nguyencuong.librarytlu.act;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nguyencuong.librarytlu.QR.Capture;
import com.nguyencuong.librarytlu.R;
import com.nguyencuong.librarytlu.config.Config;
import com.nguyencuong.librarytlu.frag.AccountFragment;
import com.nguyencuong.librarytlu.frag.HomeFragment;
import com.nguyencuong.librarytlu.model.Book;
import com.nguyencuong.librarytlu.model.LoginDataUserRespone;
import com.nguyencuong.librarytlu.retrofit.APIService;
import com.nguyencuong.librarytlu.retrofit.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityStudent extends AppCompatActivity {
    private static final String TAG = MainActivityStudent.class.getName();
    Dialog dialog;
    private Dialog dialog_process;
    BottomNavigationView navigation;
    ImageView fabBorrowBook;

    LoginDataUserRespone dataLoginStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        initView();
        addEvent();

    }

    private void addEvent() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
//                        toolbar.setTitle("Trang chủ");
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;

                    case R.id.action_account:
//                        toolbar.setTitle("Tài khoản");
                        fragment = new AccountFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });

        fabBorrowBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivityStudent.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);// optional
                integrator.setPrompt("Quét mã sách");
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);// allow barcode scanner in potrait mode
                integrator.setCaptureActivity(Capture.class);
                integrator.initiateScan();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initView() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        checkAccount();

        Fragment fragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void checkAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE);
        String json = sharedPreferences.getString("AccountUser", null);
        if (json != null) {
            Gson gson = new Gson();
            dataLoginStudent = gson.fromJson(json, LoginDataUserRespone.class);
            Log.d(TAG, dataLoginStudent.toString());
        } else {
            Intent intent = new Intent(MainActivityStudent.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void mapping() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        fabBorrowBook = findViewById(R.id.fabAddBook);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String value = result.getContents();
            if (value != null) {
                int book_id = Integer.parseInt(value);

                // do something
                showProcessDialog();

                DataService dataService = APIService.getService();
                Call<List<Book>> call = dataService.getBookInfo(book_id);
                call.enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        List<Book> listBook = response.body();
                        if (listBook != null && listBook.size() > 0) {
                            Log.d(TAG, response.toString());
                            Log.d(TAG, listBook.get(0).toString());

                            showBookDialog(listBook.get(0));
                        } else {
                            Toast.makeText(MainActivityStudent.this, "Mã sách không đúng!", Toast.LENGTH_LONG).show();
                        }
                        stopDialogProcess();
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        Log.d(TAG, t.toString());
                        Toast.makeText(MainActivityStudent.this, "Lỗi!", Toast.LENGTH_LONG).show();
                        stopDialogProcess();
                    }
                });

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showBookDialog(Book book) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_book);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        dialog.setCancelable(true);

        TextView tvNameBook = dialog.findViewById(R.id.tvNameBook);
        TextView tvNameAuthor = dialog.findViewById(R.id.tvNameAuthor);
        ImageView imvBook = dialog.findViewById(R.id.imvBook);
        ImageView imvCancel = dialog.findViewById(R.id.imvCancel);
        Button btnBorrow = dialog.findViewById(R.id.btnBorrow);


        tvNameBook.setText(book.getBooks_name());
        tvNameAuthor.setText(book.getBooks_author_name());
        Glide.
                with(this)
                .load(Config.domain + "/lms/Source/librarian/" + book.getBooks_image())
                .override(200, 200)
                .into(imvBook);

        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int book_id = Integer.parseInt(book.getId());
                showProcessDialog();
                handleBorrowBook(book_id);
            }
        });

        imvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void handleBorrowBook(int book_id) {
        int user_id = Integer.parseInt(dataLoginStudent.getId());

        DataService dataService = APIService.getService();
        Call<String> call = dataService.borrowBook(book_id, user_id);

        Log.d(TAG, call.request().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String message = response.body();
                if (message.equalsIgnoreCase("success")) {
                    Toast.makeText(MainActivityStudent.this, "Mượn thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivityStudent.this, "Lỗi! Sách đã có người mượn.", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, response.toString());

                dialog.cancel();
                initView();
                stopDialogProcess();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivityStudent.this, "Lỗi!", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());

                dialog.cancel();
                stopDialogProcess();
            }
        });

    }


    public void showProcessDialog() {
        dialog_process = new Dialog(this);
        dialog_process.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_process.setContentView(R.layout.layout_process_dialog);
        Window window = dialog_process.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        dialog_process.setCancelable(false);
        dialog_process.show();
    }

    public void stopDialogProcess() {
        if(dialog_process != null) {
            dialog_process.dismiss();
            dialog_process = null;
        }
    }
}