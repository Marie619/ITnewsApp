package app.muneef.itnewsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.News;
import app.muneef.itnewsapp.models.Users;

public class NewsApapter extends RecyclerView.Adapter<NewsApapter.NewsViewHolder> {

    private List<News> newsList;
    private Context context;


    private DatabaseReference mNewsDbRef;
    private ValueEventListener mGetLikersListListener;

    public NewsApapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        mNewsDbRef = FirebaseDatabase.getInstance().getReference().child("News");
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        holder.bind(newsList.get(position));

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProfilePoster, imgNews;
        TextView txtNews, txtTotalLikes, txtProfileName;
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

        void bind(final News news) {
            txtNews.setText(news.getPostText());

            DatabaseReference mUsersDbRef = FirebaseDatabase.getInstance().getReference().child("Users");

            ValueEventListener mGetUser = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);

                    if (user.getProfileImageUrl() != null) {

                        Glide.with(imgProfilePoster.getContext()).load(user.getProfileImageUrl()).into(imgProfilePoster);
                    }
                    txtProfileName.setText(user.getUserName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mUsersDbRef.child(news.getPosterId()).addValueEventListener(mGetUser);


            //TODO: now work for checking already liked or not

            String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (news.getLikedBy() != null) {

                if (news.getLikedBy().containsValue(currentUid)) {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp);
                    btnLike.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    btnLike.setText("Liked");
                    btnLike.setTextColor(context.getResources().getColor(R.color.iconLikeColor));
                    btnLike.setEnabled(false);
                } else {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp);
                    btnLike.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    btnLike.setText("Like");
                    btnLike.setTextColor(Color.BLACK);
                }
            }


            // Here the checking for likes is finished

            String totalLikes = String.valueOf(news.getTotalLikes());
            txtTotalLikes.setText(totalLikes);

            if (news.getPostImage() != null) {
                imgNews.setVisibility(View.VISIBLE);
                Glide.with(imgNews.getContext()).load(news.getPostImage()).into(imgNews);
            }


            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: CHANGE icon and update total likes
                    String cUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mNewsDbRef.child(news.getPostId()).child("likedBy").push().setValue(cUid);

                    int totalLikes = news.getTotalLikes() + 1;
                    mNewsDbRef.child(news.getPostId()).child("totalLikes").setValue(totalLikes);


                    Drawable img = context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp);
                    btnLike.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    btnLike.setText("Liked");
                    btnLike.setTextColor(Color.BLUE);


                }
            });
        }

    }
}
