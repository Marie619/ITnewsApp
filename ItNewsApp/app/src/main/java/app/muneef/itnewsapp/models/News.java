package app.muneef.itnewsapp.models;

import java.util.HashMap;

public class News {

    private String postId;
    private String postImage;
    private String postText;
    private int totalLikes;
    private HashMap<String, String> likedBy;
    private String posterId;


    public News() {
    }

    public News(String postId, String postImage, String postText, int totalLikes, HashMap<String,
            String> likedBy, String posterId) {
        this.postId = postId;
        this.postImage = postImage;
        this.postText = postText;
        this.totalLikes = totalLikes;
        this.likedBy = likedBy;
        this.posterId = posterId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public HashMap<String, String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(HashMap<String, String> likedBy) {
        this.likedBy = likedBy;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }
}
