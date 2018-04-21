package com.digitalgarden.gameswap.model;

import java.io.Serializable;

public class Post implements Serializable {

    public String userUid;
    public String name;
    public String price;
    public String location;
    public String description;
    public String contactEmail;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s", userUid, name, price, location, description, contactEmail);
    }

}
