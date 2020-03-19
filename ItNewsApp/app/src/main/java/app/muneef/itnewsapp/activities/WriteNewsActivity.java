package app.muneef.itnewsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.fragments.NewsFragment;
import app.muneef.itnewsapp.models.News;
import app.muneef.itnewsapp.models.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class WriteNewsActivity extends AppCompatActivity {

    CircleImageView img_user_profile;
    TextView txt_user_profile;
    ImageView img_write_status;
    EditText edtTxt_write_status;
    Button btn_import_gallery, btn_send_status;
    ProgressBar pbStatusUpload;


    Uri selectedStatusImageUri;
    Users currentUser;
    DatabaseReference mStatusDbRef;
    DatabaseReference mUsersDbRef;
    StorageReference mStatusStorageRef;
    ValueEventListener getCurrentUserListener;

    NewsFragment newsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_news);
        //For loading views in the activity
        initViews();

        mStatusDbRef = FirebaseDatabase.getInstance().getReference().child("News");
        mUsersDbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mStatusStorageRef = FirebaseStorage.getInstance().getReference().child("NewsPics");

        getCurrentUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Users.class);
                if (currentUser.getProfileImageUrl() != null) {

                    Glide.with(img_write_status.getContext()).load(currentUser.getProfileImageUrl()).into(img_user_profile);
                }
                txt_user_profile.setText(currentUser.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsersDbRef.child(currentUid).addValueEventListener(getCurrentUserListener);

        btn_import_gallery.setOnClickListener(new View.OnClickListener() {
            private static final int RC_PHOTO_PICKER = 1;

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        btn_send_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

    }

    private void initViews() {

        img_user_profile = findViewById(R.id.img_user_profile);
        txt_user_profile = findViewById(R.id.txt_user_profile);
        img_write_status = findViewById(R.id.img_write_status);
        edtTxt_write_status = findViewById(R.id.edtTxt_write_status);
        btn_import_gallery = findViewById(R.id.btn_import_gallery);
        btn_send_status = findViewById(R.id.btn_send_status);
        pbStatusUpload = findViewById(R.id.pbStatusUpload);

        newsFragment = new NewsFragment();

    }

    private void sendData() {

        String stateTxt = edtTxt_write_status.getText().toString();
        if (stateTxt.isEmpty() && selectedStatusImageUri == null) {
            Toast.makeText(WriteNewsActivity.this, "Please enter image or text", Toast.LENGTH_SHORT).show();
        } else {
            pbStatusUpload.setVisibility(View.VISIBLE);
            final String pId = mStatusDbRef.push().getKey();
            //String imgUri = selectedStatusImageUri.toString();
            String statusTxt = edtTxt_write_status.getText().toString();
            List<String> likedBy = new ArrayList<>();
            final News news = new News(pId, null, statusTxt, 0, null, FirebaseAuth.getInstance().getCurrentUser().getUid());

            if (selectedStatusImageUri != null) {
                final StorageReference statePicRef = mStatusStorageRef.child(selectedStatusImageUri.getLastPathSegment());

                statePicRef.putFile(selectedStatusImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        statePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                news.setPostImage(uri.toString());
                                mStatusDbRef.child(pId).setValue(news);
                                pbStatusUpload.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbStatusUpload.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                mStatusDbRef.child(pId).setValue(news);
                pbStatusUpload.setVisibility(View.INVISIBLE);
                startActivity(new Intent(WriteNewsActivity.this,DashboardActivity.class));
//                NewsFragment newsFragment = new NewsFragment();
//               // loadFragment(WriteNewsActivity.this,DashboardActivity.class);
            }


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedUri = data.getData();
            selectedStatusImageUri = selectedUri;
            img_write_status.setImageURI(selectedStatusImageUri);
            img_write_status.setVisibility(View.VISIBLE);
        }

    }

    private void loadFragment(FragmentActivity fragmentActivity , Fragment fragment){

        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container,fragment)
                .commit();
    }
}
