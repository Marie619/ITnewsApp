package app.muneef.itnewsapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.activities.BookViewActivity;
import app.muneef.itnewsapp.activities.DashboardActivity;
import app.muneef.itnewsapp.activities.UploadBookActivity;
import app.muneef.itnewsapp.adapters.BooksListAdapter;
import app.muneef.itnewsapp.models.Books;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksListFragment extends Fragment {

    ListView listView;
    FloatingActionButton floatingAction_books;
    RecyclerView rvBooks;

    //database reference to get uploads data
    DatabaseReference mDatabaseBookReference;
    FirebaseAuth firebaseAuth;

    //list to store uploads data
    ArrayList<Books> uploadListOfBooks;
   // String[] uploads;

    BooksListAdapter booksListAdapter ;


    public BooksListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        rvBooks = view.findViewById(R.id.rvBooks);
        floatingAction_books = view.findViewById(R.id.floatingAction_books);

        uploadListOfBooks = new ArrayList<>();

        mDatabaseBookReference = FirebaseDatabase.getInstance().getReference().child("Books");
        firebaseAuth = FirebaseAuth.getInstance();

        //Data fetching from the firebase database
        fillData();
        booksListAdapter = new BooksListAdapter(getActivity(),uploadListOfBooks);
        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBooks.setAdapter(booksListAdapter);



        floatingAction_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), UploadBookActivity.class));
            }
        });

        return view;
    }

    private void fillData() {

        mDatabaseBookReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Books books = postSnapshot.getValue(Books.class);
                    uploadListOfBooks.add(books);
                }

                booksListAdapter = new BooksListAdapter(getContext(),uploadListOfBooks);
                rvBooks.setAdapter(booksListAdapter);
                booksListAdapter.notifyDataSetChanged();


//                 uploads = new String[uploadListOfBooks.size()];
//
//                for (int i = 0; i < uploads.length; i++) {
//                    uploads[i] = uploadListOfBooks.get(i).getBookName();
//                }



                //displaying it to list
             //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(,android.R.layout.simple_list_item_1, uploads);
              //  listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Books book = uploadListOfBooks.get(i);
                    Intent intent = new Intent(getContext(), BookViewActivity.class);
                    intent.putExtra("BOOK",book);
                    startActivity(intent);
                    }
                });

                listView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mDatabaseBookReference.child(firebaseAuth.getCurrentUser().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        return true;
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }




}



