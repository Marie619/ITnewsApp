package app.muneef.itnewsapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.muneef.itnewsapp.R;

public class NewsApapter extends RecyclerView.Adapter<NewsApapter.NewsViewHolder> {

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProfilePoster,imgNews;
        TextView txtNews,txtTotalLikes,txtProfileName;
        Button btnLike;



        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNews = itemView.findViewById(R.id.img_news);
            imgProfilePoster = itemView.findViewById(R.id.img_status_profile_state);
            txtNews = itemView.findViewById(R.id.txt_news);
            txtProfileName = itemView.findViewById(R.id.txt_profile_user_state);
            txtTotalLikes = itemView.findViewById(R.id.txt_total_likes);
            btnLike = itemView.findViewById(R.id.btn_like);
        }
    }
}
