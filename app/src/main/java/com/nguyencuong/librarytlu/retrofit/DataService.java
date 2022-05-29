package com.nguyencuong.librarytlu.retrofit;

import com.nguyencuong.librarytlu.model.Book;
import com.nguyencuong.librarytlu.model.IssueReturnBookData;
import com.nguyencuong.librarytlu.model.LoginDataUserRespone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DataService {
//    @POST("/api/v1/auth/signup")
//    Call<DataLoginStudent> getDataSignup(@Body Register register);
//
//    @POST("/api/v1/auth/signin")
//    Call<DataLoginStudent> getDataSignin(@Body LoginActivity login);
//
//    @GET("/api/v1/notification/get-notification")
//    Call<DataNotification> getListNotification(@Query("page") int page
//                            , @Header("Authorization") String token);
//
//    @GET("/api/v1/home/get-home-data")
//    Call<DataHome> getDataHome(@Query("page") int page, @Query("unLock") boolean unlock
//            , @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/lms/Source/librarian/api/login.php")
    Call<List<LoginDataUserRespone>> getDataLoginStudent(@Field("username") String username, @Field("password") String password);

//    @FormUrlEncoded
//    @POST("/lms/Source/librarian/api/logint.php")
//    Call<List<DataLoginStudent>> getDataLoginLibrarian(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/lms/Source/librarian/api/issue_return_book.php")
    Call<List<IssueReturnBookData>> getBookBorrowReturn(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/lms/Source/librarian/api/books.php")
    Call<List<Book>> getBookInfo(@Field("book_id") int book_id);

    @FormUrlEncoded
    @POST("/lms/Source/librarian/api/add_issue_book.php")
    Call<String> borrowBook(@Field("book_id") int book_id, @Field("user_id") int user_id);
}
