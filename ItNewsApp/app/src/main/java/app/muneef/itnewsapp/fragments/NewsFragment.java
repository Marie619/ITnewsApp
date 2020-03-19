package app.muneef.itnewsapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.activities.WriteNewsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    FloatingActionButton floatingAction_news;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        floatingAction_news = view.findViewById(R.id.floatingAction_news);

        floatingAction_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), WriteNewsActivity.class));
                getActivity().finish();
            }
        });




        return view;
    }

}
