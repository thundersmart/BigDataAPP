package com.example.datas;

/**
 * Created by song on 2017-4-29.
 */

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Info_datas {
    private String _id;
    private String title;
    private String genres;
    private String rating;
    private String director;
    private String actors;
    private String[] keyword;
    private Bitmap pic;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public Bitmap getPic() {
        return pic;
    }
}
