package com.wenym.grooo.model.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by runzii on 16-7-2.
 */
public class School implements Parcelable {

    /**
     * id : 1
     * name : NEUQ
     */

    private int id;
    private String name;

    protected School(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
