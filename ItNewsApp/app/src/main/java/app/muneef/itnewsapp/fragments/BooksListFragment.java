package app.muneef.itnewsapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.activities.UploadBookActivity;
import app.muneef.itnewsapp.models.Books;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksListFragment extends Fragment {

    ListView listView;
    FloatingActionButton floatingAction_books;

    //database reference to get uploads data
    DatabaseReference mDatabaseBookReference;

    //list to store uploads data
    List<Books> uploadListOfBooks;


    public BooksListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        listView = view.findViewById(R.id.listView);
        floatingAction_books = view.findViewById(R.id.floatingAction_books);

        uploadListOfBooks = new ArrayList<>();

        mDatabaseBookReference = FirebaseDatabase.getInstance().getReference().child("Books");

        //Data fetching from the firebase database
        fillData();

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
                String[] uploads = new String[uploadListOfBooks.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadListOfBooks.get(i).getBookName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}



