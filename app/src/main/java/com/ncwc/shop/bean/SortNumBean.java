package com.ncwc.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaojiangjian on 15/12/8.
 */
public class SortNumBean implements Parcelable {
    String price;
    String collent;
    String sales;

    public void setCollent(String collent) {
        this.collent = collent;
    }

    public String getCollent() {
        return collent;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getPrice() {
        return price;
    }

    public String getSales() {
        return sales;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.collent);
        dest.writeString(this.sales);
    }

    public SortNumBean() {
    }

    protected SortNumBean(Parcel in) {
        this.price = in.readString();
        this.collent = in.readString();
        this.sales = in.readString();
    }

    public static final Parcelable.Creator<SortNumBean> CREATOR = new Parcelable.Creator<SortNumBean>() {
        public SortNumBean createFromParcel(Parcel source) {
            return new SortNumBean(source);
        }

        public SortNumBean[] newArray(int size) {
            return new SortNumBean[size];
        }
    };
}
