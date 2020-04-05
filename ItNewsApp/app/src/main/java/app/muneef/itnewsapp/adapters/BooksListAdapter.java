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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;

import java.util.ArrayList;
import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.Books;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksViewHolder> {

    Context context;
    ArrayList<Books> booksArrayList;

    private DatabaseReference mBookDbRef;
    FirebaseAuth firebaseAuth;
    Fetch fetch;

    public BooksListAdapter(Context context, ArrayList<Books> booksArrayList) {
        this.context = context;
        this.booksArrayList = booksArrayList;
        mBookDbRef = FirebaseDatabase.getInstance().getReference().child("Books");
        firebaseAuth = FirebaseAuth.getInstance();
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(3)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book,parent,false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BooksViewHolder holder, int position) {

        final Books books = booksArrayList.get(position);

        holder.txtBookName.setText(books.getBookName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mBookDbRef.child(firebaseAuth.getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();
                                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                return true;
            }
        });

        holder.itemView.setOnClickListener(v -> {
            String name = books.getBookName();
            String url = books.getBookUrl();
            Toast.makeText(context,url, Toast.LENGTH_LONG).show();

            downloadPdf(name,url);


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

    private void downloadPdf(String fileName,String fileUrl){

        String file = "/downloads/"+fileName;

        final Request request = new Request(fileUrl, file);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
       // request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");

        fetch.enqueue(request, updatedRequest -> {
            //Request was successfully enqueued for download.
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();
        }, error -> {
            //An error occurred enqueuing the request.
            Toast.makeText(context, "error occured"+error.toString(), Toast.LENGTH_SHORT).show();
        });
    }


}

