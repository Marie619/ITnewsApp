package app.muneef.itnewsapp.models;

import java.io.Serializable;

public class Users implements Serializable {

    private String userId;
    private String userName;
    private String userEmail;
    private String profileImageUrl;
    private String gender;


    public Users(){}

    public Users(String userId, String userName, String userEmail, String gender, String profileImageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

