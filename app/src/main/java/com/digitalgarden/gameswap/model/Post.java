package com.digitalgarden.gameswap.model;

public class Post {

    public String userId;
    public String name;
    public String price;
    public String location;
    public String description;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
