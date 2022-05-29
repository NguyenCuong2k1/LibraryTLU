package com.nguyencuong.librarytlu.frag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nguyencuong.librarytlu.R;
import com.nguyencuong.librarytlu.act.LoginActivity;
import com.nguyencuong.librarytlu.act.MainActivityStudent;
import com.nguyencuong.librarytlu.config.Config;
import com.nguyencuong.librarytlu.model.LoginDataUserRespone;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    CircleImageView imvUser;
    TextView tvName;
    Button btnLogout;
    private String TAG = AccountFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mapping(view);
        initData();
        addEvent();

        return view;
    }

    private void addEvent() {

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("SaveAccount", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                ((MainActivityStudent)getContext()).checkAccount();
            }
        });

    }

    private void initData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SaveAccount", getContext().MODE_PRIVATE);
        String json = sharedPreferences.getString("AccountUser", null);
        if(json != null) {
            Gson gson = new Gson();
            LoginDataUserRespone dataLoginStudent = gson.fromJson(json, LoginDataUserRespone.class);
            Log.d(TAG, dataLoginStudent.toString());

            Glide.
                    with(getContext())
                    .load(Config.domain + "/lms/Source/librarian/" + dataLoginStudent.getPhoto())
                    .override(200,200)
                    .into(imvUser);

            tvName.setText(dataLoginStudent.getName());
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            ((MainActivityStudent)getContext()).finish();
        }
    }

    private void mapping(View view) {
        imvUser = view.findViewById(R.id.imvUser);
        tvName = view.findViewById(R.id.tvName);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
}