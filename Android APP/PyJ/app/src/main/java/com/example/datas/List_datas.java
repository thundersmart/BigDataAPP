package com.example.datas;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by song on 2017-4-27.
 */

public class List_datas implements Parcelable {
    private String _id;
    private String title;
    private String genres;
    private String rating;
    private Bitmap pic;

    public List_datas() {
        super();
    }

    public List_datas(String _id, String title, String genres, String rating, Bitmap pic) {
        super();
        this._id = _id;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
        this.pic = pic;
    }

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

    public Bitmap getPic() {
        return pic;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化过程：必须按成员变量声明的顺序进行封装
     *
     * @param parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(_id);
        parcel.writeString(title);
        parcel.writeString(genres);
        parcel.writeString(rating);
        parcel.writeValue(pic);
    }

    /**
     * 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
     * 读取Parcel里面数据时必须按照成员变量声明的顺序，Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用  
     */
    public static final Parcelable.Creator<List_datas> CREATOR = new Creator<List_datas>() {
        @Override
        public List_datas createFromParcel(Parcel source) {
            return new List_datas(source.readString(), source.readString(), source.readString(), source.readString(), (Bitmap) source.readValue(List_datas.class.getClassLoader()));
        }

        @Override
        public List_datas[] newArray(int size) {
            return new List_datas[size];
        }
    };
}
