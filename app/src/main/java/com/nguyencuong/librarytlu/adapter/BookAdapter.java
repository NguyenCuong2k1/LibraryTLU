package com.nguyencuong.librarytlu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nguyencuong.librarytlu.R;
import com.nguyencuong.librarytlu.config.Config;
import com.nguyencuong.librarytlu.model.IssueReturnBookData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BOOK_RETURN = 0;
    private static final int TYPE_BOOK_BORROW = 1;
    private static final int TYPE_BOOK_ALL = 2;
    private static final int TYPE_ERROR = -1;
    private static final String TAG = BookAdapter.class.getName();
    Context context;
    ArrayList<IssueReturnBookData> listIssueReturnBook;

    int TYPE_SHOW = 2;

    public BookAdapter(Context context, ArrayList<IssueReturnBookData> listIssueReturnBook) {
        this.context = context;
        this.listIssueReturnBook = listIssueReturnBook;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case TYPE_BOOK_RETURN:
                view = layoutInflater.inflate(R.layout.item_book_return, parent, false);
                return new BookReturnViewHolder(view);
            case TYPE_BOOK_BORROW:
                view = layoutInflater.inflate(R.layout.item_book_borrow, parent, false);
                return new BookBorrowViewHolder(view);

            default:
                view = layoutInflater.inflate(R.layout.item_book_error, parent, false);
                return new ErrorViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IssueReturnBookData returnBookData = listIssueReturnBook.get(position);
        int type = -1;
        if(returnBookData.getReturndate() != null && !returnBookData.getReturndate().equals("")) {
            type = 0;
        } else { // Book borrowing
            type = 1;
        }

        if(TYPE_SHOW == 0 && type == 1) {
            type = -1;
        }
        if(TYPE_SHOW == 1 && type == 0) {
            type = -1;
        }

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String cost = currencyVN.format(Integer.parseInt(returnBookData.getCost()));

        switch (type) {
            case 0:
                BookReturnViewHolder bookReturnViewHolder = (BookReturnViewHolder) holder;
                bookReturnViewHolder.tvNameBook.setText(returnBookData.getBooks_name());
                bookReturnViewHolder.tvBorrowDate.setText(returnBookData.getBooksissuedate());
                bookReturnViewHolder.tvDateExpiration.setText(returnBookData.getExpirationdate());
                bookReturnViewHolder.tvCost.setText(cost);
                bookReturnViewHolder.tvReturnDate.setText(returnBookData.getReturndate());

                Glide.
                        with(context)
                        .load(Config.domain + "/lms/Source/librarian/" + returnBookData.getBooks_image())
                        .override(200,200)
                        .into(bookReturnViewHolder.imvBook);

                break;

            case 1:
//                return TYPE_BOOK_BORROW;
                BookBorrowViewHolder viewHolder = (BookBorrowViewHolder)holder;
                viewHolder.tvNameBook.setText(returnBookData.getBooks_name());
                viewHolder.tvBorrowDate.setText(returnBookData.getBooksissuedate());
                viewHolder.tvDateExpiration.setText(returnBookData.getExpirationdate());
                viewHolder.tvCost.setText(cost);
                viewHolder.tvStatus.setText("Chưa trả");

                Glide.
                        with(context)
                        .load(Config.domain + "/lms/Source/librarian/" + returnBookData.getBooks_image())
                        .override(200,200)
                        .into(viewHolder.imvBook);

                Log.d(TAG, Config.domain + "/lms/Source/librarian/" + returnBookData.getBooks_image());

                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        if(listIssueReturnBook != null) {
            return listIssueReturnBook.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        IssueReturnBookData issueReturnBookData = listIssueReturnBook.get(position);
        int type = -1;
        if(issueReturnBookData.getReturndate() != null && !issueReturnBookData.getReturndate().equals("")) {
            type = 0;
        } else { // Book borrowing
            type = 1;
        }

        if(TYPE_SHOW == 0 && type == 1) {
            type = -1;
        }
        if(TYPE_SHOW == 1 && type == 0) {
            type = -1;
        }

        switch (type) {
            case 0:
                return TYPE_BOOK_RETURN;
            case 1:
                return TYPE_BOOK_BORROW;
            case 2:
                return TYPE_BOOK_ALL;
            default:
                return TYPE_ERROR;
        }
    }

    public class BookAllViewHolder extends RecyclerView.ViewHolder {
        public BookAllViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class BookReturnViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameBook, tvDateExpiration, tvBorrowDate, tvStatus, tvCost, tvReturnDate;
        public ImageView imvBook;

        public BookReturnViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameBook = itemView.findViewById(R.id.tvNameBook);
            tvDateExpiration = itemView.findViewById(R.id.tvDateExpiration);
            tvBorrowDate = itemView.findViewById(R.id.tvDateBorrow);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCost = itemView.findViewById(R.id.tvCost);
            imvBook = itemView.findViewById(R.id.imvBook);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
        }

    }

    public class BookBorrowViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameBook, tvDateExpiration, tvBorrowDate, tvStatus, tvCost;
        public ImageView imvBook;
        public BookBorrowViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameBook = itemView.findViewById(R.id.tvNameBook);
            tvDateExpiration = itemView.findViewById(R.id.tvDateExpiration);
            tvBorrowDate = itemView.findViewById(R.id.tvDateBorrow);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCost = itemView.findViewById(R.id.tvCost);
            imvBook = itemView.findViewById(R.id.imvBook);
        }
    }

    public class ErrorViewHolder extends RecyclerView.ViewHolder {

        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setTYPE_SHOW(int TYPE_SHOW) {
        this.TYPE_SHOW = TYPE_SHOW;
        notifyDataSetChanged();
    }

}
