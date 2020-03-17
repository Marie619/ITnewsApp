package app.muneef.itnewsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.fragments.ProfileFragment;
import app.muneef.itnewsapp.models.Users;

public class ProfileActivity extends AppCompatActivity {

    private static final int RC_NAV_PHOTO_PICKER = 1;
    ImageView imgProfile;
    TextView txtUsername, txtEmailValue, txtGenderValue;

    DatabaseReference databaseReference;
    Uri selectedProfileImageUri;
    StorageReference mUsersProfilePicsStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmailValue = findViewById(R.id.txtEmailValue);
        txtGenderValue = findViewById(R.id.txtGenderValue);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersProfilePicsStorageRef = FirebaseStorage.getInstance().getReference().child("User");


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_NAV_PHOTO_PICKER);
            }
        });

         getUserInfo();


    }

    private void getUserInfo() {



        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);

                txtUsername.setText(users.getUserName());
                txtGenderValue.setText(users.getGender());
                txtEmailValue.setText(users.getUserEmail());

                Glide.with(getApplicationContext()).load(users.getProfileImageUrl()).into(imgProfile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.child(uid).addValueEventListener(valueEventListener);




    }

    private void uploadImageOnServer() {
        if (selectedProfileImageUri != null) {

            final StorageReference mPicStorageRef = mUsersProfilePicsStorageRef.child(selectedProfileImageUri.getLastPathSegment());

            mPicStorageRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mPicStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Users users = new Users();
                        users.setProfileImageUrl(uri.toString());
                            databaseReference.child(uid).child("profileImgUrl").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ProfileActivity.this, "image added successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Failed to upload profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedUri = data.getData();
            selectedProfileImageUri = selectedUri;
            imgProfile.setImageURI(selectedProfileImageUri);
            uploadImageOnServer();
        }
    }
}
