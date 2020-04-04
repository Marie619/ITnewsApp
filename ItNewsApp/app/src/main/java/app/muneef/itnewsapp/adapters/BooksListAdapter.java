package app.muneef.itnewsapp.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.Books;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksViewHolder> {

    Context context;
    ArrayList<Books> booksArrayList;

    private DatabaseReference mBookDbRef;

    public BooksListAdapter(Context context, ArrayList<Books> booksArrayList) {
        this.context = context;
        this.booksArrayList = booksArrayList;
        mBookDbRef = FirebaseDatabase.getInstance().getReference().child("Books");
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book,parent,false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {

        Books books = booksArrayList.get(position);

        holder.txtBookName.setText(books.getBookName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(context, "Long clicked", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {

            TextView txtBookName;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBookName = itemView.findViewById(R.id.txtBookName);


        }
    }


}

