package app.muneef.itnewsapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.activities.WriteNewsActivity;
import app.muneef.itnewsapp.adapters.NewsApapter;
import app.muneef.itnewsapp.models.News;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    FloatingActionButton floatingAction_news;
    RecyclerView RvNews;
    NewsApapter newsApapter;
    List<News> newsList;

    DatabaseReference mNewsDbRef;
    ChildEventListener mNewsListener;
    ValueEventListener mNewsValuesListener;
    ProgressBar progressBar_news;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        floatingAction_news = view.findViewById(R.id.floatingAction_news);
        progressBar_news = view.findViewById(R.id.progressBar_news);

        RvNews = view.findViewById(R.id.RvNews);
        progressBar_news.setVisibility(View.VISIBLE);
        mNewsDbRef = FirebaseDatabase.getInstance().getReference().child("News");

        RvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        RvNews.setHasFixedSize(true);
        newsList = new ArrayList<>();
        fillRecycler();
        newsApapter = new NewsApapter(newsList, getContext());
        RvNews.setAdapter(newsApapter);

        mNewsValuesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newsList.clear();
                newsApapter.notifyDataSetChanged();
                progressBar_news.setVisibility(View.GONE);

                for (DataSnapshot valueRes : dataSnapshot.getChildren()) {
                    News news = valueRes.getValue(News.class);
                    newsList.add(news);
                }


                newsApapter = new NewsApapter(newsList, getContext());
                RvNews.setAdapter(newsApapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mNewsDbRef.addValueEventListener(mNewsValuesListener);

        floatingAction_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), WriteNewsActivity.class));
                getActivity().finish();
            }
        });


        return view;
    }


    private void fillRecycler() {
        mNewsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // DataSnapshot dataSnapshotRes = dataSnapshot.child("Statuses");

                for (DataSnapshot valueRes : dataSnapshot.getChildren()) {
//                        Status status = valueRes.getValue(Status.class);
//                        statusList.add(status);
                }


                newsApapter = new NewsApapter(newsList, getContext());

                RvNews.setAdapter(newsApapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }

    public void notifiyChangeFromFrag() {
        newsApapter = new NewsApapter(newsList, getContext());
//        recStatusList.setAdapter(adapter);

        fillRecycler();

    }

}
