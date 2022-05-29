package com.nguyencuong.librarytlu.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nguyencuong.librarytlu.R;
import com.nguyencuong.librarytlu.act.LoginActivity;
import com.nguyencuong.librarytlu.act.MainActivityStudent;
import com.nguyencuong.librarytlu.adapter.BookAdapter;
import com.nguyencuong.librarytlu.model.IssueReturnBookData;
import com.nguyencuong.librarytlu.model.LoginDataUserRespone;
import com.nguyencuong.librarytlu.retrofit.APIService;
import com.nguyencuong.librarytlu.retrofit.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getName();
    RadioButton rbAll, rbBookBorrow, rbBookReturned;
    RadioGroup radioGroup;
    RecyclerView rvBook;
    BookAdapter bookAdapter;
    ArrayList<IssueReturnBookData> returnBookData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mapping(view);
        initData();
        addEvent();

        return view;
    }

    private void addEvent() {
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rbAll:
//                        Toast.makeText(getContext(), "All", Toast.LENGTH_LONG).show();
//                        bookAdapter.setTYPE_SHOW(2);
//                        break;
//                    case R.id.rbBookBorrow:
//                        Toast.makeText(getContext(), "Borrow", Toast.LENGTH_LONG).show();
//                        bookAdapter.setTYPE_SHOW(1);
//
//                        break;
//                    case R.id.rbBookReturned:
//                        Toast.makeText(getContext(), "Return", Toast.LENGTH_LONG).show();
//                        bookAdapter.setTYPE_SHOW(0);
//                        break;
//                }
//            }
//        });


    }

    private void initData() {
        if((MainActivityStudent)getContext() != null) {
            ((MainActivityStudent)getContext()).showProcessDialog();
        }
        rbAll.setChecked(true);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SaveAccount", getContext().MODE_PRIVATE);
        String json = sharedPreferences.getString("AccountUser", null);
        if(json != null) {
            Gson gson = new Gson();
            LoginDataUserRespone dataLoginStudent = gson.fromJson(json, LoginDataUserRespone.class);
            Log.d(TAG, dataLoginStudent.toString());

            int userID = Integer.parseInt(dataLoginStudent.getId());
            DataService dataService = APIService.getService();
            Call<List<IssueReturnBookData>> call = dataService.getBookBorrowReturn(userID);
            call.enqueue(new Callback<List<IssueReturnBookData>>() {
                @Override
                public void onResponse(Call<List<IssueReturnBookData>> call, Response<List<IssueReturnBookData>> response) {
                    returnBookData = (ArrayList<IssueReturnBookData>) response.body();

                    bookAdapter = new BookAdapter(getContext(), returnBookData);
                    rvBook.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    rvBook.setAdapter(bookAdapter);

                    if((MainActivityStudent)getContext() != null) {
                        ((MainActivityStudent)getContext()).stopDialogProcess();
                    }

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.rbAll:
                                    Toast.makeText(getContext(), "All", Toast.LENGTH_LONG).show();
                                    bookAdapter.setTYPE_SHOW(2);
                                    break;
                                case R.id.rbBookBorrow:
                                    Toast.makeText(getContext(), "Borrow", Toast.LENGTH_LONG).show();
                                    bookAdapter.setTYPE_SHOW(1);

                                    break;
                                case R.id.rbBookReturned:
                                    Toast.makeText(getContext(), "Return", Toast.LENGTH_LONG).show();
                                    bookAdapter.setTYPE_SHOW(0);
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<IssueReturnBookData>> call, Throwable t) {
                    if((MainActivityStudent)getContext() != null) {
                        ((MainActivityStudent)getContext()).stopDialogProcess();
                    }
                }
            });

        } else {
            Intent intent = new Intent((getContext()), LoginActivity.class);
            startActivity(intent);
            ((MainActivityStudent)getContext()).finish();
        }


    }

    private void mapping(View view) {
        rbAll = view.findViewById(R.id.rbAll);
        rbBookBorrow = view.findViewById(R.id.rbBookBorrow);
        rbBookReturned = view.findViewById(R.id.rbBookReturned);
        rvBook = view.findViewById(R.id.rvBook);
        radioGroup = view.findViewById(R.id.rg);
    }
}