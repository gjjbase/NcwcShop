package com.ncwc.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaojiangjian on 15/12/2.
 */
public class SortBean implements Parcelable {
    public static final Parcelable.Creator<SortBean> CREATOR = new Parcelable.Creator<SortBean>() {
        public SortBean createFromParcel(Parcel source) {
            return new SortBean(source);
        }

        public SortBean[] newArray(int size) {
            return new SortBean[size];
        }
    };
    String name;
    String id;
    String search;

    public SortBean() {
    }

    protected SortBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.search = in.readString();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.search);
    }
}
